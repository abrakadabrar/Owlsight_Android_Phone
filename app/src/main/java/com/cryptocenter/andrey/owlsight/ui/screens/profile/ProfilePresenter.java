package com.cryptocenter.andrey.owlsight.ui.screens.profile;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.utils.Screen;

@InjectViewState
public class ProfilePresenter extends BasePresenter<ProfileView> {

    void handleButtonBackClicked() {
        getViewState().closeScreen(null);
    }

    void handleButtonChangePasswordClicked() {
        getViewState().addScreen(Screen.CHANGE_PASSWORD, null);
    }

    void handleButtonChangeAccountClicked() {
        getViewState().addScreen(Screen.LEAVE_ACCOUNT, null);
    }

    void handleButtonSaveProfileClicked() {
        getViewState().closeScreen(null);
    }

    void handleButtonPhoneClicked() {
        getViewState().addScreen(Screen.CHANGE_PHONE, null);
    }
}
