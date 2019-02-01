package com.cryptocenter.andrey.owlsight.ui.screens.group;

import com.cryptocenter.andrey.owlsight.base.BaseView;
import com.cryptocenter.andrey.owlsight.data.model.Camera;

import java.util.List;

public interface GroupView extends BaseView {
    void setGroup(List<Camera> cameras);

    void showWarningDeleteGroup(Camera camera);

    void showAlertCalendar(Camera camera);

    void setCameraThumbnail(Camera camera);

    void startRefreshing();

    void completeRefreshing();

}
