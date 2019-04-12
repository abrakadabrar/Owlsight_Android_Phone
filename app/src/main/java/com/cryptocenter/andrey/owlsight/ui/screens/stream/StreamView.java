package com.cryptocenter.andrey.owlsight.ui.screens.stream;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

public interface StreamView extends BaseView {
    @StateStrategyType(SingleStateStrategy.class)
    void setupStream(String data);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setVisibilityOfConnectingLayout(boolean visible);

    @StateStrategyType(SkipStrategy.class)
    void restartActivity();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setWasDisconnected(boolean wasDisconnected);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void disposeStreamManager();

    @StateStrategyType(SkipStrategy.class)
    void startStream();
}
