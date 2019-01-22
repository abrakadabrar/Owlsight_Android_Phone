package com.cryptocenter.andrey.owlsight.ui.screens.stream;

import android.os.Handler;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.api.response.StreamResponse;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import javax.inject.Inject;

@InjectViewState
public class StreamPresenter extends BasePresenter<StreamView> {

    @Inject
    OwlsightRepository repository;

    private String streamId;
    private Handler handler = new Handler();
    private Runnable checkerStatus = () -> handleCheckStatus();


    @Inject
    StreamPresenter() {
    }

    @Override
    public void showFailed() {
        getViewState().closeScreen("Ошибка соединения с сервером");
    }

    void handlePermissionGranted(boolean isGranted) {
        if (isGranted) {
            prepareStream();
        } else {
            getViewState().closeScreen("Для работы требуется разрешение");
        }
    }

    void handleStopStream() {
        stopStream();
        stoppingHello();
    }

    void handleCheckStatus() {
        checkStatus();
        handler.postDelayed(checkerStatus, 500);
    }

    void startingHello() {
        checkerStatus.run();
    }

    void stoppingHello() {
        if (handler != null) handler.removeCallbacks(checkerStatus);
    }

    //==============================================================================================
    // Api
    //==============================================================================================

    private void prepareStream() {
        repository.prepareStream(
                getViewState()::showLoading,
                this::proceedPrepareStreamSuccess,
                this::proceedStreamFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void stopStream() {
        repository.stopStream(
                streamId,
                getViewState()::showLoading,
                this::proceedStopStreamSuccess,
                this::proceedStreamFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void checkStatus() {
        repository.statusStream(
                streamId,
                this::proceedStatusStreamSuccess,
                this::proceedStreamFailed,
                this::showError);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedPrepareStreamSuccess(StreamResponse response) {
        streamId = response.getId();
        getViewState().setupStream(response.getData());
    }

    private void proceedStopStreamSuccess(Void v) {
        getViewState().closeScreen(null);
    }

    private void proceedStatusStreamSuccess(Void v) {
    }

    private void proceedStreamFailed() {
        getViewState().closeScreen("Ошибка соединения");
    }
}
