package com.cryptocenter.andrey.owlsight.managers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.utils.CameraHelper;
import com.pedro.rtplibrary.network.AdapterBitrateParser;
import com.pedro.rtplibrary.network.ConnectionClassManager;
import com.pedro.rtplibrary.network.UploadBandwidthSampler;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;

import net.ossrs.rtmp.ConnectCheckerRtmp;


public class StreamManager implements ConnectCheckerRtmp, SurfaceHolder.Callback, ConnectionClassManager.ConnectionClassStateChangeListener, AdapterBitrateParser.Callback {

    private final StreamManagerListener streamManagerListener;
    private RtmpCamera2 streamCamera;
    private ConnectionClassManager connectionClassManager;
    private UploadBandwidthSampler uploadBandwidthSampler;
    private String url;

    public StreamManager(SurfaceView surfaceView, StreamManagerListener streamManagerListener) {
        surfaceView.getHolder().addCallback(this);
        this.streamManagerListener = streamManagerListener;
        streamCamera = new RtmpCamera2(surfaceView, this);
        connectionClassManager = ConnectionClassManager.getInstance();
        uploadBandwidthSampler = UploadBandwidthSampler.getInstance();
        connectionClassManager.register(this);
        AdapterBitrateParser.calculateMaxVideoBitrate(streamCamera.getResolutionValue());
        AdapterBitrateParser.DELAY = 100;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        streamCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (streamCamera == null) {
            return;
        }

        if (streamCamera.isRecording()) {
            streamCamera.stopRecord();

        }
        if (streamCamera.isStreaming()) {
            streamCamera.stopStream();
        }
        streamCamera.stopPreview();
    }


    @Override
    public void onConnectionSuccessRtmp() {
        streamManagerListener.onMessage("Connection success");
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        streamManagerListener.onMessage("Connection failed. " + reason);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            streamCamera.stopStream();
            streamManagerListener.onConnectionFailed();
        });
    }

    @Override
    public void onDisconnectRtmp() {
        streamManagerListener.onMessage("Disconnected");
    }

    @Override
    public void onAuthErrorRtmp() {
        streamManagerListener.onMessage("Auth error");
    }

    @Override
    public void onAuthSuccessRtmp() {
        streamManagerListener.onMessage("Auth error");
    }

    public void initStream(String url) {
        this.url = url;
        startStream();
    }

    public void startStream() {
        if (!streamCamera.isStreaming()) {
            if (streamCamera.isRecording() || prepareEncoders()) {
                uploadBandwidthSampler.startSampling();
                streamCamera.startStream(url);
            } else {
                streamManagerListener.onMessage("Error preparing stream, This device cant do it");
            }
        } else {
            uploadBandwidthSampler.stopSampling();
            streamCamera.stopStream();
        }
    }

    public void releaseStream() {
        if (streamCamera != null && streamCamera.isStreaming()) {
            streamCamera.stopStream();
            connectionClassManager.remove();
        }
    }

    public void switchCamera() {
        streamCamera.switchCamera();
    }

    public boolean checkAudioMuted() {
        if (!streamCamera.isAudioMuted()) {
            streamCamera.disableAudio();
            return true;
        } else {
            streamCamera.enableAudio();
            return false;
        }
    }

    private boolean prepareEncoders() {
        return streamCamera.prepareVideo(1280, 720, 30, 1228800, false, CameraHelper.getCameraOrientation(App.getInstance()))
                && streamCamera.prepareAudio(128 * 1024, 32000, false, false, false);
    }

//    public List<Size> getResolutionsBack() {
//        return streamCamera.getResolutionsBack();
//    }

//    public List<Size> getResolutionsFront() {
//        return streamCamera.getResolutionsFront();
//    }

    @Override
    public void onBandwidthStateChange(double bandwidth) {
        if (streamCamera != null) {
            AdapterBitrateParser.parseBitrate(streamCamera.getBitrate(), (int) bandwidth, this);
        }
    }

    @Override
    public void onNewBitrate(int bitrate) {
        if (streamCamera != null) {
            Log.d("newBitrate", "new bitrate = " + bitrate);
            streamCamera.setVideoBitrateOnFly(bitrate);
        }
    }

    public interface StreamManagerListener {
        void onMessage(String message);

        void onConnectionFailed();
    }
}

