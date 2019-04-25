package com.cryptocenter.andrey.owlsight.ui.screens.signin.forgot_password;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

public interface ForgotPasswordView extends BaseView {

    @StateStrategyType(SkipStrategy.class)
    void popBackStack();
}
