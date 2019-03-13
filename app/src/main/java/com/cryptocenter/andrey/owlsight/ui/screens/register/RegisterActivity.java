package com.cryptocenter.andrey.owlsight.ui.screens.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

import static com.cryptocenter.andrey.owlsight.ui.screens.register.RegisterPresenter.Field.CONFIRM_PASSWORD;
import static com.cryptocenter.andrey.owlsight.ui.screens.register.RegisterPresenter.Field.EMAIL;
import static com.cryptocenter.andrey.owlsight.ui.screens.register.RegisterPresenter.Field.NAME;
import static com.cryptocenter.andrey.owlsight.ui.screens.register.RegisterPresenter.Field.PASSWORD;
import static com.cryptocenter.andrey.owlsight.ui.screens.register.RegisterPresenter.Field.PHONE;

public class RegisterActivity extends BaseActivity implements RegisterView {

    @InjectPresenter
    RegisterPresenter presenter;

    @BindView(R.id.btnClose)
    ImageButton btnClose;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @BindView(R.id.tvErrorName)
    TextView tvErrorName;

    @BindView(R.id.tvErrorEmail)
    TextView tvErrorEmail;

    @BindView(R.id.tvErrorPhone)
    TextView tvErrorPhone;

    @BindView(R.id.tvErrorPassword)
    TextView tvErrorPassword;

    @BindView(R.id.tvErrorConfirmPassword)
    TextView tvErrorConfirmPassword;

    @BindView(R.id.tvErrorConfirmPolicy)
    TextView tvErrorConfirmPolicy;

    @BindView(R.id.btnLogin)
    TextView btnLogin;

    @BindView(R.id.cbConfirmPolicy)
    CheckBox cbConfirmPolicy;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    public static Intent intent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.handleResult(requestCode, resultCode);
    }

    @Override
    public void hideFieldErrors() {
        showedErrorField(false, etName, tvErrorName);
        showedErrorField(false, etEmail, tvErrorEmail);
        showedErrorField(false, etPhone, tvErrorPhone);
        showedErrorField(false, etPassword, tvErrorPassword);
        showedErrorField(false, etConfirmPassword, tvErrorConfirmPassword);
        tvErrorConfirmPolicy.setVisibility(View.GONE);
    }

    @Override
    public void showedFieldError(RegisterPresenter.Field field, boolean isShow) {
        switch (field) {
            case NAME:
                showedErrorField(isShow, etName, tvErrorName);
                break;
            case EMAIL:
                showedErrorField(isShow, etEmail, tvErrorEmail);
                break;
            case PHONE:
                showedErrorField(isShow, etPhone, tvErrorPhone);
                break;
            case PASSWORD:
                showedErrorField(isShow, etPassword, tvErrorPassword);
                break;
            case CONFIRM_PASSWORD:
                showedErrorField(isShow, etConfirmPassword, tvErrorConfirmPassword);
                break;
            case CONFIRM_POLICY:
                tvErrorConfirmPolicy.setVisibility(isShow ? View.VISIBLE : View.GONE);
                break;
        }
    }


    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        btnClose.setOnClickListener(v -> finish());
        btnLogin.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> presenter.handleRegisterClick(
                etName.getText().toString(),
                etEmail.getText().toString(),
                etPhone.getText().toString(),
                etPassword.getText().toString(),
                etConfirmPassword.getText().toString(),
                cbConfirmPolicy.isChecked())
        );

        etName.setOnFocusChangeListener((v, hasFocus) -> presenter.handleFieldFocusChanged(etName.getText(), hasFocus, NAME));
        etEmail.setOnFocusChangeListener((v, hasFocus) -> presenter.handleFieldFocusChanged(etEmail.getText(), hasFocus, EMAIL));
        etPhone.setOnFocusChangeListener((v, hasFocus) -> presenter.handleFieldFocusChanged(etPhone.getText(), hasFocus, PHONE));
        etPassword.setOnFocusChangeListener((v, hasFocus) -> presenter.handleFieldFocusChanged(etPassword.getText(), hasFocus, PASSWORD));
        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> presenter.handleFieldFocusChanged(etConfirmPassword.getText(), hasFocus, CONFIRM_PASSWORD));
        cbConfirmPolicy.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.handleConfirmPolicyChanged(isChecked));
        MaskedTextChangedListener listener = new MaskedTextChangedListener("+7 ([000]) [000]-[00]-[00]", etPhone);
        etPhone.setHint(listener.placeholder());
        etPhone.addTextChangedListener(listener);
        etPhone.setOnFocusChangeListener(listener);
    }

    private void showedErrorField(boolean isShowed, EditText field, TextView tvError) {
        field.setCompoundDrawablesWithIntrinsicBounds(0, 0, isShowed ? R.drawable.ic_error : 0, 0);
        tvError.setVisibility(isShowed ? View.VISIBLE : View.GONE);
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    RegisterPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(RegisterPresenter.class);
    }
}
