package com.cryptocenter.andrey.owlsight.ui.screens.change_phone;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePhoneDialogFragment extends BaseDialogFragment implements ChangePhoneView {

    public static ChangePhoneDialogFragment newInstance() {

        Bundle args = new Bundle();

        ChangePhoneDialogFragment fragment = new ChangePhoneDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter
    ChangePhonePresenter presenter;

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etConfirmationCode)
    EditText etConfirmationCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_change_phone;
    }

    @OnClick({R.id.btnClose, R.id.btnSendConfirmationCode, R.id.btnSavePhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                presenter.handleButtonCloseClicked();
                break;
            case R.id.btnSendConfirmationCode:
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
