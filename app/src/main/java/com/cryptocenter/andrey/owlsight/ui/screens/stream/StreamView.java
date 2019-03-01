package com.cryptocenter.andrey.owlsight.ui.screens.stream;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

public interface StreamView extends BaseView {
    void setupStream(String data);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setVisibilityOfConnectingLayout(boolean visible);

    @StateStrategyType(SkipStrategy.class)
    void restartActivity();

}
