package com.cryptocenter.andrey.owlsight.ui.screens.groups;

import com.cryptocenter.andrey.owlsight.base.BaseView;
import com.cryptocenter.andrey.owlsight.data.model.Group;

import java.util.List;

interface GroupsView extends BaseView {

    void setGroups(List<Group> groups);

    void showAlertGroupEdit(int position, String title);

    void setGroupTitle(String title);

    void restart();
}
