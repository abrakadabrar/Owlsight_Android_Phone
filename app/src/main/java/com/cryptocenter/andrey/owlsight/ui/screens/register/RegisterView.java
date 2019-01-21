package com.cryptocenter.andrey.owlsight.ui.screens.register;

import com.cryptocenter.andrey.owlsight.base.BaseView;

interface RegisterView extends BaseView {
    void hideFieldErrors();
    void showedFieldError(RegisterPresenter.Field field, boolean isShow);
}
