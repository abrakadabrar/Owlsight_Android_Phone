package com.cryptocenter.andrey.owlsight.ui.screens.signin.activity;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;

@InjectViewState
public class SignInActivityPresenter extends BasePresenter<SignInActivityView> {

    @Override
    protected void onFirstViewAttach() {
        getViewState().setSignInFragment();
    }

    void handleForgotPassword() {
        getViewState().setForgotPasswordFragment();
    }
}
