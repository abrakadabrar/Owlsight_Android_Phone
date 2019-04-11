package com.cryptocenter.andrey.owlsight.ui.screens.camera_options;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.event.DeleteCameraEvent;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

@InjectViewState
public class CameraOptionsPresenter extends BasePresenter<CameraOptionsView> {

    private final OwlsightRepository owlsightRepository;
    private final Integer cameraId;
    private final Preferences preferences;

    private boolean isInRepositoryTransaction = false;

    @Inject
    CameraOptionsPresenter(OwlsightRepository owlsightRepository, Integer cameraId, Preferences preferences) {
        this.owlsightRepository = owlsightRepository;
        this.cameraId = cameraId;
        this.preferences = preferences;
    }

    void onMotionDetectionCheckChanged(boolean checked) {
        if (checked) {
            owlsightRepository.addMotionNotification(cameraId, preferences.getFirebaseNotificationToken(), null, null, null, null, null);
        } else {
            owlsightRepository.deleteMotionNotification(cameraId, preferences.getFirebaseNotificationToken(), null, null, null, null, null);
        }
    }

    void onBtnMotionDetectionClicked() {
        getViewState().toggleSwitch();
    }

    void onBtnDeleteCameraClicked() {
        getViewState().setConfirmationLayoutVisible();
    }

    void onBtnYesClicked(){
        owlsightRepository.deleteCamera(cameraId,
                getViewState()::showLoading,
                result -> {
                    EventBus.getDefault().post(new DeleteCameraEvent());
                    getViewState().closeDialog();
                },
                () -> getViewState().showMessage(App.getInstance().getString(R.string.could_not_remove_camera)),
                error -> getViewState().showMessage(App.getInstance().getString(R.string.could_not_remove_camera)),
                () -> getViewState().hideLoading());
    }

    void onBtnNoClicked() {
        getViewState().setMainLayoutVisible();
    }

    void onBtnCloseOptionsDialogClicked() {
        getViewState().closeDialog();
    }
}
