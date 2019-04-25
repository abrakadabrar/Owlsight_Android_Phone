package com.cryptocenter.andrey.owlsight.ui.screens.change_password;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;

@InjectViewState
public class ChangePasswordPresenter extends BasePresenter<ChangePasswordView> {

    void handleButtonCloseClicked() {
        getViewState().dismissDialog();
    }

    void handleButtonSavePhoneClicked() {
        getViewState().dismissDialog();
    }
}
