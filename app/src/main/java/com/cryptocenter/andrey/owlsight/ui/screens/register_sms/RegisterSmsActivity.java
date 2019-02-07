package com.cryptocenter.andrey.owlsight.ui.screens.register_sms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.R;
//import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

public class RegisterSmsActivity extends BaseActivity implements RegisterSmsView {

    @InjectPresenter
    RegisterSmsPresenter presenter;

    @BindView(R.id.btnClose)
    ImageButton btnClose;

    @BindView(R.id.etCode)
    PinEntryEditText etCode;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvTime)
    TextView tvTime;

    //private SmsVerifyCatcher smsVerifyCatcher;

    public static Intent intent(Context context, RegisterData registerData) {
        final Intent intent = new Intent(context, RegisterSmsActivity.class);
        intent.putExtra("registerData", registerData);
        return intent;
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sms);
        ButterKnife.bind(this);
        presenter.setRegisterData((RegisterData) getIntent().getSerializableExtra("registerData"));
        setupUI();
    }

    //@Override
    //public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //}

    @Override
    public void onBackPressed() {
        presenter.handleBackClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //smsVerifyCatcher.onStop();
    }


    // =============================================================================================
    // View
    // =============================================================================================


    @Override
    public void showPhoneLabel(String label) {
        tvDescription.setText(label);
    }

    @Override
    public void showTimer(String time) {
        tvTime.setText(time);
    }


    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        //smsVerifyCatcher = new SmsVerifyCatcher(this, message -> presenter.handleCodeInput(message.replaceAll("\\D+","")));
        btnClose.setOnClickListener(v -> finish());
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.handleCodeInput(etCode.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    RegisterSmsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(RegisterSmsPresenter.class);
    }
}
