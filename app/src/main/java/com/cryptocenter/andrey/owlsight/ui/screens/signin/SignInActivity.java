package com.cryptocenter.andrey.owlsight.ui.screens.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.di.Scopes;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class SignInActivity extends BaseActivity implements SignInView {

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

    public static Intent intent(Context context) {
        return new Intent(context, SignInActivity.class);
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        etEmail.setText("kvazar@gmail.com");
        etEmail.setSelection(etEmail.getText().toString().length());
        etPassword.setText("fado5518");
        etPassword.setSelection(etPassword.getText().toString().length());

        btnLogin.setOnClickListener(v -> presenter.handleLoginClick(etEmail.getText().toString(), etPassword.getText().toString()));
        tvSignUp.setOnClickListener(v -> presenter.handleRegistrationClick());
        btnLogin.callOnClick();
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    SignInPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(SignInPresenter.class);
    }
}
