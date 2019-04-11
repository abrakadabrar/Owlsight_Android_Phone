package com.cryptocenter.andrey.owlsight.ui.screens.group;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.data.model.api.response.RecordsResponse;
import com.cryptocenter.andrey.owlsight.data.model.data.FromDateData;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import java.util.List;

import javax.inject.Inject;

import static com.cryptocenter.andrey.owlsight.utils.Screen.ADD_CAMERA;
import static com.cryptocenter.andrey.owlsight.utils.Screen.GROUPS;
import static com.cryptocenter.andrey.owlsight.utils.Screen.SINGLE_PLAYER;

@InjectViewState
public class GroupPresenter extends BasePresenter<GroupView> {

    private OwlsightRepository repository;
    private List<Camera> cameras;
    private final String groupId;
    private boolean handleCameraResponce = true;
    private boolean isLoading = false;


    @Inject
    GroupPresenter(OwlsightRepository repository, List<Camera> cameras, String groupId) {
        this.repository = repository;
        this.cameras = cameras;
        this.groupId = groupId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setGroup(cameras);
    }

    void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
//        getViewState().startRefreshing();
//        getViewState().setGroup(cameras);

    }

    void handleDeleteGroupClick(Camera camera) {
        getViewState().showWarningDeleteGroup(camera);
    }

    void handleCalendarClick(Camera camera) {
        getCalendarData(camera);
    }

    void handleWarningDeleteOkClick(Camera camera) {
        deleteGroup(camera);
    }

    void handleDateSelect(Camera camera, String date) {
        getViewState().addScreen(Screen.VIDEO_FROM_DATE, new FromDateData(camera.getCameraId(), date));
    }

    void refresh() {
        getViewState().refreshGroups();
//        getViewState().startRefreshing();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getViewState().completeRefreshing();
//            }
//        }, 4000);
    }

    void handleCameraResponce(boolean handle) {
        this.handleCameraResponce = handle;
    }

    void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }


    //==============================================================================================
    // API
    //==============================================================================================

    public void showFailed() {
        getViewState().showMessage(App.getInstance().getString(R.string.cannot_perform_action));
    }

    void handleGetThumbnail(Camera camera) {
        repository.getCameraThumbnail(camera.getCameraId(), data -> proceedGetCameraThumbnailSuccess(camera, data));
    }

    void handleGetThumbnailUpload(Camera camera) {
        repository.getCameraThumbnailUpdated(camera.getCameraId(), data -> proceedGetCameraThumbnailUploadSuccess(camera, data));
    }

    void handleCameraClick(Camera camera) {
        repository.getCamera(
                camera.getCameraId(),
                () -> {
                    if (!isLoading) {
                        isLoading = true;
                        getViewState().showLoading();
                    }
                },
                this::proceedGetCameraSuccess,
                () -> {
                    if (handleCameraResponce) {
                        handleCameraClick(camera);
                    }
                },
                error -> {
                    if (handleCameraResponce) {
                        handleCameraClick(camera);
                    }
                },
                () -> {

                });
    }

    void addCamera() {
        getViewState().addScreen(ADD_CAMERA, groupId);
    }

    private void deleteGroup(Camera camera) {
        repository.deleteGroup(
                camera.getGroupId(),
                getViewState()::showLoading,
                this::proceedDeleteGroupSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void getCalendarData(Camera camera) {
        repository.getCameraRecords(
                camera.getCameraId(),
                getViewState()::showLoading,
                records -> proceedGetCameraRecordsSuccess(camera, records),
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedGetCameraSuccess(String id) {
        if (handleCameraResponce) {
            getViewState().addScreen(SINGLE_PLAYER, id);
        }
    }

    private void proceedGetCameraThumbnailSuccess(Camera camera, String data) {
        camera.setThumbnailUrl(data);
        getViewState().setCameraThumbnail(camera);
    }

    private void proceedGetCameraThumbnailUploadSuccess(Camera camera, String data) {
        camera.setRefreshing(false);
        camera.setThumbnailUrl(data);
        getViewState().setCameraThumbnail(camera);
    }

    private void proceedGetCameraRecordsSuccess(Camera camera, RecordsResponse camRecords) {
        camera.setFolders(camRecords.getCalendarData());
        getViewState().showAlertCalendar(camera);
    }

    private void proceedDeleteGroupSuccess(Void v) {
        getViewState().showMessage(App.getInstance().getString(R.string.group_deleted));
        getViewState().showScreen(GROUPS);
    }

    void onOptionsButtonClicked(Integer cameraId) {
        getViewState().showOptionsDialog(cameraId);
    }
}
