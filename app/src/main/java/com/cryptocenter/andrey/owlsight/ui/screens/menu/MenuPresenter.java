package com.cryptocenter.andrey.owlsight.ui.screens.menu;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class MenuPresenter extends BasePresenter<MenuView> {

    @Inject
    OwlsightRepository repository;

    void handleCloseClick() {
        getViewState().onBackClicked();
    }

    void handleStreamModeClick() {
        getViewState().addScreen(Screen.STREAM, null);
    }

    void handleMonitorsModeClick() {
        fetchMonitors();
    }

    void handleMonitorSelected(Monitor monitor) {
        getViewState().addScreen(Screen.MONITOR, monitor);
    }

    //==============================================================================================
    // API
    //==============================================================================================

    private void fetchMonitors() {
        repository.getMonitors(
                getViewState()::showLoading,
                this::proceedMonitorsSuccess,
                this::onFetchMonitorsFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    //==============================================================================================
    // Private
    //==============================================================================================

    private void onFetchMonitorsFailed(){
        proceedMonitorsSuccess(new ArrayList<>());
    }
    private void proceedMonitorsSuccess(List<Monitor> monitors) {
        if (monitors.isEmpty()) {
            getViewState().showMessage("Пока тут пусто");
        } else {
            getViewState().showAlertMonitors(monitors);
        }
    }
}
