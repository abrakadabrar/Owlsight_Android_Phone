package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import android.os.Handler;

import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.ui.screens.add_group.AddGroupView;
import com.cryptocenter.andrey.owlsight.ui.screens.launcher.LauncherView;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import javax.inject.Inject;

public class AddCameraPresenter extends BasePresenter<AddGroupView> {

    @Inject
    AddCameraPresenter() {
        new Handler().postDelayed(() -> getViewState().showScreen(Screen.SIGN_IN), 2000);
    }

}
