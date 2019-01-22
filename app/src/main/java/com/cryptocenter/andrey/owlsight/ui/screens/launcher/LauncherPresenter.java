package com.cryptocenter.andrey.owlsight.ui.screens.launcher;

import android.os.Handler;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import javax.inject.Inject;

@InjectViewState
public class LauncherPresenter extends BasePresenter<LauncherView> {

    @Inject
    LauncherPresenter() {
        new Handler().postDelayed(() -> getViewState().showScreen(Screen.SIGN_IN), 2000);
    }
}
