package com.cryptocenter.andrey.owlsight.managers;

import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.utils.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;


public class StreamManager implements ConnectCheckerRtmp, SurfaceHolder.Callback {

    private final StreamManagerListener streamManagerListener;
    private RtmpCamera2 streamCamera;

    public StreamManager(OpenGlView openGlView, StreamManagerListener streamManagerListener) {
        this.streamManagerListener = streamManagerListener;
        streamCamera = new RtmpCamera2(openGlView, this);
        openGlView.getHolder().addCallback(this);
        openGlView.enableAA(true);
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
        if (!streamCamera.isStreaming()) {
            if (streamCamera.isRecording() || prepareEncoders()) {
                streamCamera.startStream(url);
            } else {
                streamManagerListener.onMessage("Error preparing stream, This device cant do it");
            }
        } else {
            streamCamera.stopStream();
        }
    }

    public void releaseStream() {
        if (streamCamera != null && streamCamera.isStreaming()) {
            streamCamera.stopStream();
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

    public interface StreamManagerListener {
        void onMessage(String message);

        void onConnectionFailed();
    }

    public void dropContext() {
        streamCamera.dropContext();
    }
}
