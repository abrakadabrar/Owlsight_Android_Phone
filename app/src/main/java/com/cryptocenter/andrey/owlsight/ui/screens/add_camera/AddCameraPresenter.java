package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import android.os.Handler;

import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.api.response.AddCameraResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.TestCameraResponse;
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

    /*
    rtsp://owlsight:20170731a@212.30.190.77:54411/axis-media/media.amp?streamprofile=owlsight
     */

    public void testCamera(String uesrname, String password,String host, String port, String extra){
        repository.testCamera(
                createLink(uesrname, password,host, port, extra),
                getViewState()::showLoading,
                this::handleTest,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }
    private String createLink(String uesrname, String password,String host, String port, String extra){
        String form = "rtsp://%s1:%s2@%s3:%s4/axis-media/media.amp?streamprofile=owlsight";


        return form;
    }

    private void handleTest(TestCameraResponse response){

    }

    public void addCamera(String cameraName,
                          String host, String port,
                          String request, String login,
                          String password, String tz){
        repository.addCamera(
                this.groupId, cameraName, host,
                Integer.parseInt(port), request, login, password, tz,
                getViewState()::showLoading,
                this::handleAdding,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void handleAdding(AddCameraResponse response){

    }

}
