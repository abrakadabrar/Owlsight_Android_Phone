package com.cryptocenter.andrey.owlsight.ui.screens.choose_group;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

import java.util.List;

public interface ChooseGroupView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showGroups(List<String> groupNames);

    @StateStrategyType(SkipStrategy.class)
    void addCamera(Integer cameraId);

    @StateStrategyType(SkipStrategy.class)
    void finishNoResult();
}
