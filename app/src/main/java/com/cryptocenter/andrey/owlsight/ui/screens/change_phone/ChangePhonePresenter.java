package com.cryptocenter.andrey.owlsight.ui.screens.change_phone;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;

@InjectViewState
public class ChangePhonePresenter extends BasePresenter<ChangePhoneView> {
    void handleButtonCloseClicked() {
        getViewState().dismissDialog();
    }

    void handleButtonSavePhoneClicked() {
        getViewState().dismissDialog();
    }
}
