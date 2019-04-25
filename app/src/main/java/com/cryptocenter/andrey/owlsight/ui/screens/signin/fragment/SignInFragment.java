package com.cryptocenter.andrey.owlsight.ui.screens.signin.fragment;

import android.app.AlertDialog;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseFragment;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.custom.InitFingerPrintDialog;
import com.cryptocenter.andrey.owlsight.utils.FingerPrint.CryptoUtils;
import com.cryptocenter.andrey.owlsight.utils.FingerPrint.Fingerprint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Toothpick;

public class SignInFragment extends BaseFragment implements SignInView, Fingerprint.OnAuthenticationFingerprintCallback {

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter
    SignInPresenter presenter;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.tvSignUp)
    AppCompatTextView tvSignUp;

    private Preferences preferences;
    private Fingerprint mFingerprint;
    private AlertDialog alertDialog;


    // =============================================================================================
    // Android
    // =============================================================================================


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        preferences = new Preferences(getContext());
        setupUI();
    }

    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        if (preferences.isLogin()) {
            String[] loginData = preferences.getLoginData();
            etEmail.setText(loginData[0]);
            etPassword.setText(loginData[1]);
        } else {
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
    private void initFingerPrint() {
        if (Fingerprint.isSensorStateAt(Fingerprint.SensorState.READY, getContext())) {
            FingerprintManager.CryptoObject cryptoObject = CryptoUtils.getCryptoObject();
            mFingerprint = new Fingerprint(getContext());
            mFingerprint.setOnAuthenticationFingerprintCallback(this);
            mFingerprint.startAuth(getContext(), cryptoObject);
            alertDialog = new InitFingerPrintDialog(getContext());
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferences.isFingerAuth()) {
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
        preferences.saveLoginData(etEmail.getText().toString(), etPassword.getText().toString());
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
        Toast.makeText(getContext(), R.string.fingerprint_doesnt_fit, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFingerprintError(String error) {
//        Toast.makeText(this, "ERROR!!! "+error,Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnForgotPassword)
    public void onButtonForgotPasswordClicked() {
        presenter.handleButtonForgotPasswordClicked();
    }
}
