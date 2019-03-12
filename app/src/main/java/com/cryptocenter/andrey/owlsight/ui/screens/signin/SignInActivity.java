package com.cryptocenter.andrey.owlsight.ui.screens.signin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.custom.InitFingerPrintDialog;
import com.cryptocenter.andrey.owlsight.utils.FingerPrint.CryptoUtils;
import com.cryptocenter.andrey.owlsight.utils.FingerPrint.Fingerprint;

import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class SignInActivity extends BaseActivity implements SignInView,Fingerprint.OnAuthenticationFingerprintCallback {

    @InjectPresenter
    SignInPresenter presenter;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.tvSignUp)
    TextView tvSignUp;

    private Preferences preferences;
    private Fingerprint mFingerprint;
    private AlertDialog alertDialog;

    public static Intent intent(Context context) {
        return new Intent(context, SignInActivity.class);
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in_up);
        ButterKnife.bind(this);
        setupUI();
    }


    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        if(preferences.isLogin()){
            String[] loginData = preferences.getLoginData();
            etEmail.setText(loginData[0]);
            etPassword.setText(loginData[1]);
        }else {
            etEmail.setText("kvazar@gmail.com");
            etPassword.setText("fado5518");
        }
        etEmail.setSelection(etEmail.getText().toString().length());
        etPassword.setSelection(etPassword.getText().toString().length());

        btnLogin.setOnClickListener(v -> presenter.handleLoginClick(etEmail.getText().toString(), etPassword.getText().toString()));
        tvSignUp.setOnClickListener(v -> presenter.handleRegistrationClick());
       // btnLogin.callOnClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initFingerPrint(){
        if(Fingerprint.isSensorStateAt(Fingerprint.SensorState.READY,this)){
            FingerprintManager.CryptoObject cryptoObject = CryptoUtils.getCryptoObject();
            mFingerprint = new Fingerprint(this);
            mFingerprint.setOnAuthenticationFingerprintCallback(this);
            mFingerprint.startAuth(this,cryptoObject);
            alertDialog = new InitFingerPrintDialog(this);
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(preferences.isFingerAuth()) {
            String[] loginData = preferences.getLoginData();
            etEmail.setText(loginData[0]);
            etPassword.setText(loginData[1]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                initFingerPrint();
            }
        }
    }

    @Override
    public void saveLoginData() {
        preferences.saveLoginData(etEmail.getText().toString(),etPassword.getText().toString());
    }

    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    SignInPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(SignInPresenter.class);
    }


    @Override
    public void onAuthenticationFingerprintSucceeded() {
        alertDialog.dismiss();
        btnLogin.callOnClick();
    }

    @Override
    public void onAuthenticationFingerprintFailed(boolean isCanceled) {

        Toast.makeText(this, "Отпечаток не подходит. Повторите еще раз.",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAcuthenticationFingerprintError(String error) {
//        Toast.makeText(this, "ERROR!!! "+error,Toast.LENGTH_LONG).show();
    }
}
