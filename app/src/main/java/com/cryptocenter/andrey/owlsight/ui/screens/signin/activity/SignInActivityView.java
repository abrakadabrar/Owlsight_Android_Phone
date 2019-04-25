package com.cryptocenter.andrey.owlsight.ui.screens.signin.activity;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

public interface SignInActivityView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSignInFragment();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setForgotPasswordFragment();
}
