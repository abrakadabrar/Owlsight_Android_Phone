package com.cryptocenter.andrey.owlsight.managers.video_download

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.crashlytics.android.Crashlytics
import com.cryptocenter.andrey.owlsight.R
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository
import com.cryptocenter.andrey.owlsight.di.Scopes
import com.cryptocenter.andrey.owlsight.ui.screens.groups.GroupsActivity
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import toothpick.Toothpick
import java.io.*
import javax.inject.Inject
import kotlin.random.Random

class DownloadVideoManager : Service() {

    @Inject
    lateinit var owlsightRepository: OwlsightRepository

    private lateinit var videoPath: String
    private lateinit var cameraId: String

    private lateinit var disposable: Disposable

    private lateinit var notificationManager: NotificationManager

    private val notificationId = Random(System.currentTimeMillis()).nextInt()

    private lateinit var mainHandler: Handler

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mainHandler = Handler(mainLooper)
            setupChannels()

            owlsightRepository = Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository::class.java)
            videoPath = intent.getStringExtra(VIDEO_PATH)
            cameraId = intent.getStringExtra(CAMERA_ID)

            startDownloading()
        } catch (e: Exception) {
            Crashlytics.logException(e)
        }


        return Service.START_REDELIVER_INTENT
    }

    private fun startDownloading() {
        val directory = File(Environment.getExternalStorageDirectory(), "Owlsight/Records")
        directory.mkdirs()

        val file = File(directory, String.format("date_%s_id_%s.mp4", videoPath.replace('/', '_').replace(".mp4", ""), cameraId))
        disposable = owlsightRepository.downloadVideo(videoPath, cameraId)
                .doOnComplete {
                    stopSelf()
                }
                .subscribe({ responseBody ->
                    if (isExternalStorageWritable()) {
                        showMessage(getString(R.string.start_downloading))
                        writeResponseBodyToDisk(responseBody, file)
                    }
                }, { error ->
                    Crashlytics.logException(Exception("Cant download video", error))
                    error.printStackTrace()
                })
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @Throws(Exception::class)
    fun writeResponseBodyToDisk(body: ResponseBody, file: File): Boolean {
        try {
            file.createNewFile()

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                startForeground(notificationId, notificationBuilder(fileSize.toInt(), 0).build())
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    notificationManager.notify(notificationId, notificationBuilder(fileSize.toInt(), fileSizeDownloaded.toInt()).setContentText("""Файл ${file.name} скачивается...""").build())
                }

                val fileString = getString(R.string.file)
                val loaded = getString(R.string.loaded)
                val notifyMessage = """$fileString ${file.name} $loaded."""

                notificationManager.notify(notificationId, notification(notifyMessage).build())
                showMessage(notifyMessage)
                stopForeground(false)

                outputStream.flush()

                return true
            } catch (e: IOException) {
                Crashlytics.logException(Exception("Cant download video", e))
                e.printStackTrace()
                return false
            } finally {
                inputStream?.close()

                outputStream?.close()
            }
        } catch (e: IOException) {
            Crashlytics.logException(Exception("Cant download video", e))
            e.printStackTrace()
            return false
        }
    }

    private fun setupChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val adminChannel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH)
            adminChannel.description = CHANNEL_ID
            adminChannel.setSound(null, null)
            adminChannel.enableLights(false)
            adminChannel.lightColor = Color.RED
            adminChannel.enableVibration(false)
            notificationManager.createNotificationChannel(adminChannel)
        }
    }

    companion object {

        fun startService(context: Context, videoPath: String, cameraId: String) {
            val videoIntent = Intent(context, DownloadVideoManager::class.java)
            videoIntent.putExtra(VIDEO_PATH, videoPath)
            videoIntent.putExtra(CAMERA_ID, cameraId)
            context.startService(videoIntent)
        }

        private const val VIDEO_PATH = "VIDEO_URL"
        private const val CAMERA_ID = "CAMERA_ID"
        private const val CHANNEL_ID = "OWLSIGHT_CHANNEL"
    }

    private fun notificationBuilder(max: Int, progress: Int): NotificationCompat.Builder {
        val notificationIntent = Intent(this, GroupsActivity::class.java)

        val contentIntent = PendingIntent.getActivity(baseContext, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setAutoCancel(false)
                .setSound(null)
                .setProgress(max, progress, false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.video_loading))
                .setWhen(System.currentTimeMillis())
    }

    private fun notification(text: String): NotificationCompat.Builder {
        val notificationIntent = Intent(this, GroupsActivity::class.java)

        val contentIntent = PendingIntent.getActivity(baseContext, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setAutoCancel(false)
                .setSound(null)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(text)
                .setWhen(System.currentTimeMillis())
    }


    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun showMessage(message: String) {
        mainHandler.post {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
