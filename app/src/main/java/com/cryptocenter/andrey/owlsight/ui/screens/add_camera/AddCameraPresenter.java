package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.api.response.AddCameraResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.TestCameraResponse;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import javax.inject.Inject;

public class AddCameraPresenter extends BasePresenter<AddCameraView> {

    private int groupId;
    private OwlsightRepository repository;
    private boolean isTestSuccess = false;

    @Inject
    AddCameraPresenter(OwlsightRepository repository, Integer groupId) {
        this.repository = repository;
        this.groupId = groupId;
        // new Handler().postDelayed(() -> getViewState().showScreen(Screen.ADD_CAMERA));
    }

    /*
    rtsp://owlsight:20170731a@212.30.190.77:54411/axis-media/media.amp?streamprofile=owlsight
     */

    public void nextClick(String cameraName, String host, String port, String request, String login, String password, String tz){
        if(!isTestSuccess){
            testCamera(login,password,host,port,request);
        } else {
            addCamera(cameraName,host,port,request,login,password,"-11:00");
        }
    }

    private void testCamera(String uesrname, String password, String host, String port, String extra) {
        repository.testCamera(
                createLink(uesrname, password, host, port, extra),
                getViewState()::showLoading,
                this::handleTest,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private String createLink(String uesrname, String password, String host, String port, String extra) {
        String form = "rtsp://%1$s:%2$s@%3$s:%4$s/%5$s";
        String result = String.format(form, uesrname, password, host, port, extra);

        return result;
    }

    private void handleTest(TestCameraResponse response) {
        isTestSuccess = response.isPinged();
        if(isTestSuccess){
            getViewState().showTestResult("success");
        } else {
            getViewState().showTestResult("fail");
        }
    }

    public void addCamera(String cameraName,
                          String host, String port,
                          String request, String login,
                          String password, String tz) {
        repository.addCamera(
                this.groupId, cameraName, host,
                Integer.parseInt(port), request, login, password, tz,
                getViewState()::showLoading,
                this::handleAdding,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void handleAdding(AddCameraResponse response) {
        if(response.isSuccess()){
            getViewState().closeScreen("");
        } else {
            getViewState().showTestResult("error");
        }
    }

}
