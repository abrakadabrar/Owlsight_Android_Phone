package com.cryptocenter.andrey.owlsight.ui.screens.change_phone;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

public interface ChangePhoneView extends BaseView {
    @StateStrategyType(SkipStrategy.class)
    void dismissDialog();
}
