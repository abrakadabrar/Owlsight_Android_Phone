package com.cryptocenter.andrey.owlsight.ui.screens.camera_options;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

public interface CameraOptionsView extends BaseView {
    void toggleSwitch();
    void closeDialog();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setMainLayoutVisible();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setConfirmationLayoutVisible();
}
