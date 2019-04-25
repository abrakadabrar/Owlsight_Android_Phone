package com.cryptocenter.andrey.owlsight.ui.screens.leave_profile;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

public interface LeaveProfileView extends BaseView {
    @StateStrategyType(SkipStrategy.class)
    void dismissDialog();
}
