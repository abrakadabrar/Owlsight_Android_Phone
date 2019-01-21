package com.cryptocenter.andrey.owlsight.ui.screens.monitor;

import com.cryptocenter.andrey.owlsight.base.BaseView;
import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;

import java.util.List;

public interface MonitorView extends BaseView {
    void setMonitors(List<MonitorCamera> monitors);
    void setRunningMonitor(MonitorCamera camera);
}
