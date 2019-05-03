package com.cryptocenter.andrey.owlsight.ui.screens.change_phone;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseDialogFragment;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MaskedTextChangedListener listener = new MaskedTextChangedListener("+7 ([000]) [000]-[00]-[00]", etPhone);
        etPhone.setHint(listener.placeholder());
        etPhone.addTextChangedListener(listener);
        etPhone.setOnFocusChangeListener(listener);
    }

    @OnClick({R.id.btnClose, R.id.btnSendConfirmationCode, R.id.btnSavePhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                presenter.handleButtonCloseClicked();
                break;
            case R.id.btnSendConfirmationCode:
                presenter.handleButtonSendConfirmationCode(etPhone.getText().toString());
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
