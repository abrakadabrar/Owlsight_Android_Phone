package com.cryptocenter.andrey.owlsight.ui.screens.leave_profile;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.utils.Screen;

@InjectViewState
public class LeaveProfilePresenter extends BasePresenter<LeaveProfileView> {

    Preferences preferences;

    LeaveProfilePresenter() {
        preferences = new Preferences(App.getInstance());
    }

    void handleButtonYesClicked() {
        preferences.cleanPreferences();
        getViewState().closeScreen(null);
        getViewState().addScreen(Screen.SIGN_IN, null);
    }

    void handleButtonNoClicked() {
        getViewState().dismissDialog();
    }
}
