package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Toothpick;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class AddCameraActivity extends BaseActivity implements AddCameraView {

    private static final String ID = "AddCameraActivity.ID";

    @InjectPresenter
    AddCameraPresenter presenter;
    @BindView(R.id.btnClose)
    ImageButton btnClose;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etCamName)
    TextInputEditText etCamName;
    @BindView(R.id.tvErrorCamName)
    TextView tvErrorCamName;
    @BindView(R.id.etCamHost)
    TextInputEditText etCamHost;
    @BindView(R.id.tvErrorCamHost)
    TextView tvErrorCamHost;
    @BindView(R.id.etCamPort)
    TextInputEditText etCamPort;
    @BindView(R.id.tvErrorCamPort)
    TextView tvErrorCamPort;
    @BindView(R.id.etCamExtra)
    TextInputEditText etCamExtra;
    @BindView(R.id.tvErrorCamOptions)
    TextView tvErrorCamOptions;
    @BindView(R.id.etCamUser)
    TextInputEditText etCamUser;
    @BindView(R.id.tvErrorCamUser)
    TextView tvErrorCamUser;
    @BindView(R.id.tvErrorCamPass)
    TextView tvErrorCamPass;
    @BindView(R.id.textViewdata)
    TextView textViewdata;
    @BindView(R.id.etCamPassword)
    TextView etPassword;
    @BindView(R.id.btnCamNext)
    Button btnCamNext;


    public static Intent intent(Context context, int cameraId) {
        final Intent intent = new Intent(context, AddCameraActivity.class);
        intent.putExtra(ID, cameraId);
        return intent;
    }

    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_camera);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnCamNext)
    public void onViewClicked() {
        presenter.addCamera(etCamName.getText().toString(),
                etCamHost.getText().toString(),
                etCamPort.getText().toString(),
                etCamExtra.getText().toString(),
                etCamUser.getText().toString(),
                etCamUser.getText().toString(),
                etPassword.getText().toString());
    }

    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    AddCameraPresenter providePresenter() {
        return new AddCameraPresenter(
                Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository.class),
                getIntent().getIntExtra(ID, -1));
    }


    @Override
    public void showTestResult(String message) {
        textViewdata.setText(message);
    }
}


