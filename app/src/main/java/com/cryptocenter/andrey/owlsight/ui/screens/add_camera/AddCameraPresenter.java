package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import android.os.Handler;

import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.ui.screens.add_group.AddGroupView;
import com.cryptocenter.andrey.owlsight.ui.screens.launcher.LauncherView;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import javax.inject.Inject;

public class AddCameraPresenter extends BasePresenter<AddCameraView> {

    private int groupId;
    private OwlsightRepository repository;

    @Inject
    AddCameraPresenter(OwlsightRepository repository, Integer groupId) {
        this.repository = repository;
        this.groupId = groupId;
       // new Handler().postDelayed(() -> getViewState().showScreen(Screen.ADD_CAMERA));
    }

}
