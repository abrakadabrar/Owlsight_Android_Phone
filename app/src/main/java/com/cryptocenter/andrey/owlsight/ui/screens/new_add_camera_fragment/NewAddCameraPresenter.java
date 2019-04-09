package com.cryptocenter.andrey.owlsight.ui.screens.new_add_camera_fragment;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.utils.Screen;

@InjectViewState
public class NewAddCameraPresenter extends BasePresenter<NewAddCameraView> {

    void onBtnEnterCameraModeClicked() {
        getViewState().addScreen(Screen.STREAM, null);
    }

    void onBtnAddIpCameraClicked() {
        getViewState().addScreen(Screen.CHOOSE_GROUP, null);
    }
}
