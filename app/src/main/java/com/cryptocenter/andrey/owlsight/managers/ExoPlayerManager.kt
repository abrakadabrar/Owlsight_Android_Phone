package com.cryptocenter.andrey.owlsight.managers

import android.content.Context
import android.net.Uri
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * @author repitch
 */
class ExoPlayerManager(private val context: Context) : Player.EventListener {

    val exoPlayer: ExoPlayer = ExoPlayerFactory.newSimpleInstance(context)

    var onLoadingChangedListener: OnLoadingChangedListener? = null
    var onPlayEndedListener: OnPlayEndedListener? = null

    fun setToView(playerView: PlayerView) {
        playerView.player = exoPlayer
    }

    fun prepare(uri: Uri) {
        val dataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(context, "Owlsight")
        ).apply {
            defaultRequestProperties.set("Cookie", Preferences(context).cookie)
            defaultRequestProperties.set("Content-Type", "application/x-mpegURL")
            defaultRequestProperties.set("Accept", "*/*")
        }

        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)

        exoPlayer.playWhenReady = true
        exoPlayer.addListener(this)
        exoPlayer.prepare(videoSource)
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        onLoadingChangedListener?.onLoadingChanged(isLoading)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_ENDED) {
            onPlayEndedListener?.onPlayEnded()
        }
    }

    interface OnLoadingChangedListener {
        fun onLoadingChanged(isLoading: Boolean)
    }

    interface OnPlayEndedListener {
        fun onPlayEnded()
    }
}
