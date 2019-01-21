package com.cryptocenter.andrey.owlsight.ui.screens.menu;

import com.cryptocenter.andrey.owlsight.base.BaseView;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;

import java.util.List;

interface MenuView extends BaseView {
    void showAlertMonitors(List<Monitor> monitors);
}
