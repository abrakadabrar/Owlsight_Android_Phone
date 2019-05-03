package com.cryptocenter.andrey.owlsight.ui.screens.change_phone;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;

@InjectViewState
public class ChangePhonePresenter extends BasePresenter<ChangePhoneView> {
    void handleButtonCloseClicked() {
        getViewState().dismissDialog();
    }

    void handleButtonSavePhoneClicked() {
        getViewState().dismissDialog();
    }

    void handleButtonSendConfirmationCode(String phone) {
        if (phone.length() != 18) {
            getViewState().showMessage(R.string.valid_phone_error);
            return;
        }
    }
}
