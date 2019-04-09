package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.event.AddCameraEvent;
import com.cryptocenter.andrey.owlsight.data.model.api.response.AddCameraResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.TestCameraResponse;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

@InjectViewState
public class AddCameraPresenter extends BasePresenter<AddCameraView> {

    private boolean test = false;

    public static final String TEST_USERNAME = "owlsight";
    public static final String TEST_PASSWORD = "20170731a";
    public static final String TEST_HOST = "212.30.190.77";
    public static final String TEST_PORT = "54411";
    public static final String TEST_EXTRA = "axis-media/media.amp?streamprofile=owlsight";
    public static final String TEST_NAME = "TEST";

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

    public void nextClick(String cameraName, String host, String port, String request, String login, String password, String tz) {
        if (!isTestSuccess) {
            testCamera(login, password, host, port, request);
        } else {
            addCamera(cameraName, host, port, request, login, password, "-11:00");
        }
    }

    private void testCamera(String username, String password, String host, String port, String extra) {
        repository.testCamera(createLink(test ? TEST_USERNAME : username, test ? TEST_PASSWORD : password, test ? TEST_HOST : host, test ? TEST_PORT : port, test ? TEST_EXTRA : extra),
                getViewState()::showLoading,
                this::handleTest,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private String createLink(String username, String password, String host, String port, String extra) {
        String form = "rtsp://%1$s:%2$s@%3$s:%4$s/%5$s";
        String result = String.format(form, username, password, host, port, extra);

        return result;
    }

    private void handleTest(TestCameraResponse response) {
        isTestSuccess = response.isPinged();
        if (isTestSuccess) {
            getViewState().testResult(true);
        } else {
            getViewState().testResult(false);
        }
    }

    public void addCamera(String cameraName,
                          String host, String port,
                          String request, String login,
                          String password, String tz) {
        repository.addCamera(
                this.groupId, test ? TEST_NAME : cameraName, test ? TEST_HOST : host,
                Integer.parseInt(test ? TEST_PORT : port), test ? TEST_EXTRA : request, test ? TEST_USERNAME : login, test ? TEST_PASSWORD : password, tz,
                getViewState()::showLoading,
                this::handleAdding,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void handleAdding(AddCameraResponse response) {
        if (response.isSuccess()) {
            getViewState().addResult(true);
            EventBus.getDefault().post(new AddCameraEvent(groupId));

        } else {
            getViewState().addResult(false);
        }
    }

}
