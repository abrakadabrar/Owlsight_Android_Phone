package com.cryptocenter.andrey.owlsight.ui.screens.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements ProfileView {

    public static void start(Context context) {
        Intent starter = new Intent(context, ProfileActivity.class);
        context.startActivity(starter);
    }

    @InjectPresenter
    ProfilePresenter presenter;

    @BindView(R.id.etName)
    AppCompatEditText etName;
    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.tvUserId)
    AppCompatTextView tvUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnBack, R.id.btnChangePassword, R.id.btnChangeAccount, R.id.btnSaveProfile, R.id.btnPhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                presenter.handleButtonBackClicked();
                break;
            case R.id.btnChangePassword:
                presenter.handleButtonChangePasswordClicked();
                break;
            case R.id.btnChangeAccount:
                presenter.handleButtonChangeAccountClicked();
                break;
            case R.id.btnSaveProfile:
                presenter.handleButtonSaveProfileClicked();
                break;
            case R.id.btnPhone:
                presenter.handleButtonPhoneClicked();
                break;
        }
    }
}
