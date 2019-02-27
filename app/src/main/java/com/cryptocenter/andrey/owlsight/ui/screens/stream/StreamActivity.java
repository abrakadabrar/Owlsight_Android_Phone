package com.cryptocenter.andrey.owlsight.ui.screens.stream;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.managers.StreamManager;
import com.cryptocenter.andrey.owlsight.utils.Permissions;
import com.pedro.encoder.input.video.CameraOpenException;

import butterknife.ButterKnife;
import toothpick.Toothpick;

public class StreamActivity extends BaseActivity implements StreamView, View.OnClickListener {

    @InjectPresenter
    StreamPresenter presenter;
    private StreamManager streamManager;

    public static Intent intent(Context context) {
        return new Intent(context, StreamActivity.class);
    }

    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_streaming);
        ButterKnife.bind(this);
        setupUI();

        streamManager = new StreamManager(this);
        Permissions.checkCamera(this, presenter::handlePermissionGranted);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCameraSwitch:
                try {
                    streamManager.switchCamera();
                } catch (CameraOpenException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnMicrophone:
                ((ImageView) view).setImageResource(streamManager.checkAudioMuted() ? R.drawable.microphone_off : R.drawable.microphone_on);
                break;
            case R.id.btnClose:
                onBackClicked();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        streamManager.releaseStream();
        presenter.handleStopStream();
        finish();
    }

    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setupStream(String url) {
        streamManager.initStream(findViewById(R.id.openGlView), url);
        presenter.startingHello();
    }


    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        findViewById(R.id.btnCameraSwitch).setOnClickListener(this);
        findViewById(R.id.btnMicrophone).setOnClickListener(this);
        findViewById(R.id.btnClose).setOnClickListener(this);
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    StreamPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(StreamPresenter.class);
    }
}
