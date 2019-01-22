package com.cryptocenter.andrey.owlsight.managers;

import android.app.Activity;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.cryptocenter.andrey.owlsight.utils.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

public class StreamManager implements ConnectCheckerRtmp, SurfaceHolder.Callback {

    private Activity context;
    private RtmpCamera1 streamCamera;
    private Handler handler;

    public StreamManager(Activity context) {
        this.context = context;
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
        if (streamCamera.isStreaming()) {
            streamCamera.stopStream();
        }
        streamCamera.stopPreview();
    }


    @Override
    public void onConnectionSuccessRtmp() {
        context.runOnUiThread(() -> Toast.makeText(context, "Connection success", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        context.runOnUiThread(() -> {
            Toast.makeText(context, "Connection failed. " + reason, Toast.LENGTH_SHORT).show();
            streamCamera.stopStream();
        });
    }

    @Override
    public void onDisconnectRtmp() {
        context.runOnUiThread(() -> Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAuthErrorRtmp() {
        context.runOnUiThread(() -> Toast.makeText(context, "Auth error", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAuthSuccessRtmp() {
        context.runOnUiThread(() -> Toast.makeText(context, "Auth success", Toast.LENGTH_SHORT).show());
    }

    public void initStream(SurfaceView surfaceView, String url) {
        streamCamera = new RtmpCamera1(surfaceView, this);
        surfaceView.getHolder().addCallback(this);
        if (!streamCamera.isStreaming()) {
            if (streamCamera.isRecording() || streamCamera.prepareAudio() && streamCamera.prepareVideo() || prepareEncoders()) {
                streamCamera.startStream(url);
            } else {
                Toast.makeText(context, "Error preparing stream, This device cant do it", Toast.LENGTH_SHORT).show();
            }
        } else {
            streamCamera.stopStream();
        }
    }

    public void releaseStream() {
        if (streamCamera != null) streamCamera.stopStream();
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
        return streamCamera.prepareVideo(720, 1024, 10,
                720 * 1024,
                false, CameraHelper.getCameraOrientation(context))
                && streamCamera.prepareAudio(128 * 1024, 720 * 1024, false, false, false);
    }
}
