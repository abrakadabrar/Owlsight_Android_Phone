package com.cryptocenter.andrey.owlsight.base;

import com.arellomobile.mvp.MvpPresenter;

public abstract class BasePresenter<View extends BaseView> extends MvpPresenter<View> {

    @Override
    public void attachView(View view) {
        super.attachView(view);
        getViewState().bind();
    }

    public void handleBackClick() {
        getViewState().onBackClicked();
    }

    public void showFailed() {
        getViewState().showFailed();
    }

    public void showError(Throwable error) {
        getViewState().showError(error);
    }
}
