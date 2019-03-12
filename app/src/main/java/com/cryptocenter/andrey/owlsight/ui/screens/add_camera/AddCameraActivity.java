package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddCameraActivity extends BaseActivity implements AddCameraView, TextWatcher {

    private static final String ID = "AddCameraActivity.ID";

    @InjectPresenter
    AddCameraPresenter presenter;
    @BindView(R.id.btnClose)
    ImageButton btnClose;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etCamName)
    TextInputEditText etCamName;
    @BindView(R.id.etCamHost)
    TextInputEditText etCamHost;
    @BindView(R.id.tvErrorCamName)
    TextView tvErrorCamName;
    @BindView(R.id.etCamPort)
    TextInputEditText etCamPort;
    @BindView(R.id.tvErrorCamHost)
    TextView tvErrorCamHost;
    @BindView(R.id.etCamExtra)
    TextInputEditText etCamExtra;
    @BindView(R.id.tvErrorCamPort)
    TextView tvErrorCamPort;
    @BindView(R.id.etCamUser)
    TextInputEditText etCamUser;
    @BindView(R.id.tvErrorCamOptions)
    TextView tvErrorCamOptions;
    @BindView(R.id.etCamPassword)
    TextInputEditText etCamPassword;
    @BindView(R.id.tvErrorCamUser)
    TextView tvErrorCamUser;
    @BindView(R.id.tvErrorCamPass)
    TextView tvErrorCamPass;
    @BindView(R.id.textViewdata)
    EditText textViewdata;
    @BindView(R.id.btnCamNext)
    Button btnCamNext;
    @BindView(R.id.tv_add_camera_help)
    TextView tvHelp;
    @BindView(R.id.tv_add_camera_test_status)
    TextView tvTestStatus;


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
        setListeners();
    }

    private void setListeners(){
        etCamExtra.addTextChangedListener(this);
        etCamHost.addTextChangedListener(this);
        etCamPassword.addTextChangedListener(this);
        etCamPort.addTextChangedListener(this);
        etCamUser.addTextChangedListener(this);
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
    public void testResult(boolean isSuccess) {
        if(isSuccess) {
            tvTestStatus.setText("Получен ответ от камеры");
            btnCamNext.setText("Добавить");
        } else {
            tvTestStatus.setText("Нет ответа от камеры, проверьте камеру или данные");
        }
        tvTestStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void addResult(boolean isSuccess) {
        if(isSuccess) {
            finish();
        } else {
            tvTestStatus.setText("Не удалось, повторите позже");
        }
    }

    @OnClick({R.id.btnClose, R.id.btnCamNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnCamNext:
                presenter.nextClick(etCamName.getText().toString(),etCamHost.getText().toString(),etCamPort.getText().toString(),
                        etCamExtra.getText().toString(),etCamUser.getText().toString(),etCamPassword.getText().toString(),"");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        textViewdata.setText(createLink(etCamUser.getText().toString(),etCamPassword.getText().toString(),etCamHost.getText().toString(),etCamPort.getText().toString(),etCamExtra.getText().toString()));
    }


    private String createLink(String uesrname, String password, String host, String port, String extra) {
        String form = "rtsp://%1$s:%2$s@%3$s:%4$s/%5$s";
        String result = String.format(form, uesrname, password, host, port, extra);

        return result;
    }
}


