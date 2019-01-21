package com.cryptocenter.andrey.owlsight.ui.screens.monitor;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.ui.screens.monitor.adapter.MonitorAdapter;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import javax.inject.Inject;

@InjectViewState
public class MonitorPresenter extends BasePresenter<MonitorView> implements MonitorAdapter.OnMonitorCameraListener {

    private OwlsightRepository repository;
    private Monitor monitor;

    @Inject
    MonitorPresenter(OwlsightRepository repository, Monitor monitor) {
        this.repository = repository;
        this.monitor = monitor;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setMonitors(monitor.getCams());
    }

    @Override
    public void onMonitorClick(MonitorCamera monitorCamera) {
        getViewState().addScreen(Screen.SINGLE_PLAYER, monitorCamera.getId());
    }

    @Override
    public void onMonitorGetInfo(MonitorCamera monitorCamera) {
        fetchMonitorCameraInfo(monitorCamera);
    }


    //==============================================================================================
    // Api
    //==============================================================================================

    private void fetchMonitorCameraInfo(MonitorCamera monitor) {
        repository.getMonitorInfo(
                monitor,
                this::proceedGetCameraInfoSuccess,
                this::showFailed,
                this::showError);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedGetCameraInfoSuccess(MonitorCamera monitor) {
        monitor.setHasRequestInfo(true);
        getViewState().setRunningMonitor(monitor);
    }
}
