package com.cryptocenter.andrey.owlsight.ui.screens.change_password;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseDialogFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.change_phone.ChangePhoneView;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordFragmentDialog extends BaseDialogFragment implements ChangePasswordView {
    public static ChangePasswordFragmentDialog newInstance() {

        Bundle args = new Bundle();
        ChangePasswordFragmentDialog fragment = new ChangePasswordFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.etCurrentPassword)
    EditText etCurrentPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etNewPasswordAgain)
    EditText etNewPasswordAgain;

    @InjectPresenter
    ChangePasswordPresenter presenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_change_password;
    }

    @OnClick({R.id.btnClose, R.id.btnSavePhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                presenter.handleButtonCloseClicked();
                break;
            case R.id.btnSavePhone:
                presenter.handleButtonSavePhoneClicked();
                break;
        }
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }
}
