package com.cryptocenter.andrey.owlsight.ui.screens.groups;

import com.cryptocenter.andrey.owlsight.base.BaseView;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;

import java.util.List;

interface GroupsView extends BaseView {

    void setGroups(List<Group> groups);

    void setGroupsRefreshed(List<Group> groups,String refreshingName);

    void showAlertGroupEdit(int position, String title);

    void setGroupTitle(String title);

    void restart();

    void showAlertMonitors(List<Monitor> monitors);

    void hideScreens();

    void proceedBtnCameraModeClicked();
}
