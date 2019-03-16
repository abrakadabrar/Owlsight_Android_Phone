package com.cryptocenter.andrey.owlsight.managers;

import android.app.Activity;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.cryptocenter.andrey.owlsight.utils.CameraHelper;
import com.pedro.rtplibrary.network.AdapterBitrateParser;
import com.pedro.rtplibrary.network.ConnectionClassManager;
import com.pedro.rtplibrary.network.UploadBandwidthSampler;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

public class StreamManager implements ConnectCheckerRtmp, SurfaceHolder.Callback, ConnectionClassManager.ConnectionClassStateChangeListener, AdapterBitrateParser.Callback {

    private Activity activity;
    private final OnDisconnectListener onDisconnectListener;
    private RtmpCamera2 streamCamera;
    private Handler handler;
    private ConnectionClassManager connectionClassManager;
    private UploadBandwidthSampler uploadBandwidthSampler;

    public StreamManager(Activity activity, OnDisconnectListener onDisconnectListener) {
        this.activity = activity;
        this.onDisconnectListener = onDisconnectListener;
        connectionClassManager = ConnectionClassManager.getInstance();
        uploadBandwidthSampler = UploadBandwidthSampler.getInstance();
        connectionClassManager.register(this);
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
        activity.runOnUiThread(() -> Toast.makeText(activity, "Connection success", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, "Connection failed. " + reason, Toast.LENGTH_SHORT).show();
            streamCamera.stopStream();
            onDisconnectListener.onRtmpDisconnect();

        });
    }

    @Override
    public void onDisconnectRtmp() {
        activity.runOnUiThread(() -> Toast.makeText(activity, "Disconnected", Toast.LENGTH_SHORT).show());
        onDisconnectListener.onRtmpDisconnect();
    }

    @Override
    public void onAuthErrorRtmp() {
        activity.runOnUiThread(() -> Toast.makeText(activity, "Auth error", Toast.LENGTH_SHORT).show());
        onDisconnectListener.onRtmpDisconnect();
    }

    @Override
    public void onAuthSuccessRtmp() {
        activity.runOnUiThread(() -> Toast.makeText(activity, "Auth success", Toast.LENGTH_SHORT).show());
    }

    public void initStream(OpenGlView openGlView, String url) {
        streamCamera = new RtmpCamera2(openGlView, this);
        openGlView.getHolder().addCallback(this);
        openGlView.enableAA(true);
        AdapterBitrateParser.calculateMaxVideoBitrate(streamCamera.getResolutionValue());
        AdapterBitrateParser.DELAY = 10000;
        if (!streamCamera.isStreaming()) {
            if (streamCamera.isRecording() || prepareEncoders()) {
                uploadBandwidthSampler.startSampling();
                streamCamera.startStream(url);
            } else {
                Toast.makeText(activity, "Error preparing stream, This device cant do it", Toast.LENGTH_SHORT).show();
            }
        } else {
            uploadBandwidthSampler.stopSampling();
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
        return streamCamera.prepareVideo(1280, 720, 30, 1228800, false, CameraHelper.getCameraOrientation(activity))
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
        streamCamera.setVideoBitrateOnFly(bitrate);
        connectionClassManager.remove();
    }

    public interface OnDisconnectListener{
        void onRtmpDisconnect();
    }
}
