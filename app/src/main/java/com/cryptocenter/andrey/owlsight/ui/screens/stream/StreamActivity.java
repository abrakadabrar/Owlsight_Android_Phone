package com.cryptocenter.andrey.owlsight.ui.screens.stream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.managers.StreamManager;
import com.novoda.merlin.Connectable;
import com.novoda.merlin.Disconnectable;
import com.novoda.merlin.Merlin;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.view.OpenGlView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

public class StreamActivity extends BaseActivity implements StreamView, Connectable, Disconnectable {

    public static int STREAM_REQUEST_CODE = 114;

    @InjectPresenter
    StreamPresenter presenter;
    @BindView(R.id.llTryConnect)
    LinearLayout llTryConnect;
    @BindView(R.id.openGlView)
    OpenGlView openGlView;

    private StreamManager streamManager;
    private Merlin merlin;
    private RxPermissions rxPermissions;
    private Unbinder unbinder;

    private boolean wasDisconnected = false;

    public static Intent intent(Context context) {
        return new Intent(context, StreamActivity.class);
    }

    public static void start(Activity context) {
        Intent starter = new Intent(context, StreamActivity.class);
        context.startActivityForResult(starter, STREAM_REQUEST_CODE);
    }

    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_streaming);
        unbinder = ButterKnife.bind(this);

        initMerlin();

        rxPermissions = new RxPermissions(this);
    }

    private void initMerlin() {
        merlin = new Merlin
                .Builder()
                .withDisconnectableCallbacks()
                .withConnectableCallbacks()
                .build(this);

        merlin.registerConnectable(this);
        merlin.registerDisconnectable(this);
    }

    @OnClick({R.id.btnCameraSwitch, R.id.btnMicrophone, R.id.btnClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCameraSwitch:
                try {
                    streamManager.switchCamera();
                } catch (CameraOpenException e) {
                    Toast.makeText(App.getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnMicrophone:
                ((ImageView) view).setImageResource(streamManager.checkAudioMuted() ? R.drawable.microphone_off : R.drawable.microphone_on);
                break;
            case R.id.btnClose:
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (streamManager != null) {
            streamManager.releaseStream();
        }
        presenter.handleStopStream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        merlin.unbind();
    }

    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setupStream(String url) {
        streamManager = new StreamManager(openGlView, new StreamManager.StreamManagerListener() {
            @Override
            public void onMessage(String message) {
                showMessageOnUiThread(message);
            }

            @Override
            public void onConnectionFailed() {
                onDisconnect();
            }
        });

        streamManager.initStream(url);
        presenter.startingHello();
    }

    @Override
    public void setVisibilityOfConnectingLayout(boolean visible) {
        runOnUiThread(() -> {
            if (visible) {
                llTryConnect.setVisibility(View.VISIBLE);
            } else {
                llTryConnect.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        destroy();
        super.onDestroy();
    }

    private void destroy() {
        if (streamManager != null) {
            streamManager.dropContext();
            streamManager = null;
        }
        unbinder.unbind();
        merlin = null;
        rxPermissions = null;

    }

    @Override
    public void restartActivity() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void setWasDisconnected(boolean wasDisconnected) {
        this.wasDisconnected = wasDisconnected;
    }


    @Override
    public void onConnect() {
        if (wasDisconnected) {
            disposeOnDestroy(rxPermissions.
                    request(CAMERA, RECORD_AUDIO)
                    .subscribe(presenter::handlePermissionGrantedOnDisconnected));
        } else {
            disposeOnDestroy(rxPermissions.
                    request(CAMERA, RECORD_AUDIO)
                    .subscribe(presenter::handlePermissionGranted));
        }

    }


    @Override
    public void onDisconnect() {
        presenter.handleDisconnect();
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    StreamPresenter providePresenter() {
        return new StreamPresenter();
    }

    private void showMessageOnUiThread(String message) {
        runOnUiThread(() -> Toast.makeText(App.getInstance(), message, Toast.LENGTH_LONG).show());
    }
}
