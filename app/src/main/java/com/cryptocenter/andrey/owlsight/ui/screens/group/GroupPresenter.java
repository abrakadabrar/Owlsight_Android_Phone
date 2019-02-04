package com.cryptocenter.andrey.owlsight.ui.screens.group;

import android.os.Handler;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.data.model.api.response.RecordsResponse;
import com.cryptocenter.andrey.owlsight.data.model.api.response.Response;
import com.cryptocenter.andrey.owlsight.data.model.data.FromDateData;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import java.util.List;

import javax.inject.Inject;

import static com.cryptocenter.andrey.owlsight.utils.Screen.GROUPS;
import static com.cryptocenter.andrey.owlsight.utils.Screen.SINGLE_PLAYER;

@InjectViewState
public class GroupPresenter extends BasePresenter<GroupView> {

    private OwlsightRepository repository;
    private List<Camera> cameras;
    private boolean handleCameraResponce = true;
    private boolean isLoading = false;


    @Inject
    GroupPresenter(OwlsightRepository repository, List<Camera> cameras) {
        this.repository = repository;
        this.cameras = cameras;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setGroup(cameras);
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

    void refresh(){
        getViewState().startRefreshing();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getViewState().completeRefreshing();
//            }
//        }, 4000);
    }

    void handleCameraResponce(boolean handle){
        this.handleCameraResponce = handle;
    }

    void setLoading(boolean isLoading){
        this.isLoading = isLoading;
    }


    //==============================================================================================
    // API
    //==============================================================================================

    public void showFailed() {
        getViewState().showMessage("Не удалось выполнить действие. Попробуйте позже");
    }

    void handleGetThumbnail(Camera camera) {
        repository.getCameraThumbnail(camera.getCameraId(), data -> proceedGetCameraThumbnailSuccess(camera, data));
    }

    void handleCameraClick(Camera camera) {
        repository.getCamera(
                camera.getCameraId(),
                new Response.Start() {
                    @Override
                    public void onStart() {
                        if(!isLoading) {
                            isLoading = true;
                            getViewState().showLoading();
                        }
                    }
                },
                this::proceedGetCameraSuccess,
                new Response.Failed() {
                    @Override
                    public void onFailed() {
                        if (handleCameraResponce) {
                            handleCameraClick(camera);
                        }
                    }
                },
                new Response.Error() {
                    @Override
                    public void onError(Throwable error) {
                        if (handleCameraResponce) {
                            handleCameraClick(camera);
                        }
                    }
                },
                new Response.Complete() {
                    @Override
                    public void onComplete() {

                    }
                });
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
        if(handleCameraResponce) {
            getViewState().addScreen(SINGLE_PLAYER, id);
        }
    }

    private void proceedGetCameraThumbnailSuccess(Camera camera, String data) {
        camera.setThumbnailUrl(data);
        getViewState().setCameraThumbnail(camera);
    }

    private void proceedGetCameraRecordsSuccess(Camera camera, RecordsResponse camRecords) {
        camera.setFolders(camRecords.getCalendarData());
        getViewState().showAlertCalendar(camera);
    }

    private void proceedDeleteGroupSuccess(Void v) {
        getViewState().showMessage("Группа удалена");
        getViewState().showScreen(GROUPS);
    }
}
