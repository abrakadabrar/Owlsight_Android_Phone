package com.cryptocenter.andrey.owlsight.ui.screens.group;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;
import com.cryptocenter.andrey.owlsight.data.model.Camera;

import java.util.List;

public interface GroupView extends BaseView {
    void setGroup(List<Camera> cameras);

    void showWarningDeleteGroup(Camera camera);

    @StateStrategyType(SkipStrategy.class)
    void showAlertCalendar(Camera camera);

    void setCameraThumbnail(Camera camera);

    void startRefreshing();

    void completeRefreshing();

    void refreshGroups();

    @StateStrategyType(SkipStrategy.class)
    void showOptionsDialog(Integer cameraId);
}