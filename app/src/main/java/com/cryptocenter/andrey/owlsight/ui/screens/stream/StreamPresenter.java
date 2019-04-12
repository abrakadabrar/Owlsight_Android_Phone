package com.cryptocenter.andrey.owlsight.ui.screens.stream;

import android.os.Handler;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.api.response.StreamResponse;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;

import javax.inject.Inject;

import toothpick.Toothpick;

@InjectViewState
public class StreamPresenter extends BasePresenter<StreamView> {

    @Inject
    OwlsightRepository repository;

    private String streamId;
    private Handler handler = new Handler();
    private Runnable checkerStatus = this::handleCheckStatus;
    private boolean netDisconnected = false;

    StreamPresenter() {
        repository = Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository.class);
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

    private void stopStream() {
        repository.stopStream(
                streamId,
                getViewState()::showLoading,
                this::proceedStopStreamSuccess,
                this::proceedStreamFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    void handlePermissionGrantedOnDisconnected(boolean isGranted) {
        netDisconnected = false;
        getViewState().setWasDisconnected(false);
        if (isGranted) {
            getViewState().restartActivity();
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
        handler.postDelayed(checkerStatus, 5000);
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

    private void checkStatus() {
        repository.statusStream(
                streamId,
                this::proceedStatusStreamSuccess,
                this::proceedStreamFailed,
                this::showError);
    }

    public void handleDisconnect() {
        netDisconnected = true;
        getViewState().disposeStreamManager();
        getViewState().setVisibilityOfConnectingLayout(true);
        getViewState().setWasDisconnected(true);
        stoppingHello();
    }

    void handleManagerDisconnect() {
        if(!netDisconnected){
//            getViewState().disposeStreamManager();
            new Handler().postDelayed(() -> getViewState().startStream(), 3000);
        }
    }

    public void handleMaxFramesDiscarded() {
        getViewState().setVisibilityOfConnectingLayout(true);
        getViewState().restartActivity();
        stoppingHello();

    }
    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedPrepareStreamSuccess(StreamResponse response) {
        streamId = response.getId();
        getViewState().setVisibilityOfConnectingLayout(false);
        getViewState().setupStream(response.getData());
    }

    private void proceedStopStreamSuccess(Void v) {
        getViewState().closeScreen(null);
    }

    private void proceedStatusStreamSuccess(Void v) {
    }

    private void proceedStreamFailed() {

    }


}
