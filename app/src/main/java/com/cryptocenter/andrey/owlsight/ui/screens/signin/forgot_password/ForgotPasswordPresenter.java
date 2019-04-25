package com.cryptocenter.andrey.owlsight.ui.screens.signin.forgot_password;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;

@InjectViewState
public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordView> {
    void handleButtonSendNewPasswordClicked() {

    }

    void handleButtonAuthorizationClicked() {
        getViewState().popBackStack();
    }
}
