package com.cryptocenter.andrey.owlsight.base;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.utils.Screen;
@StateStrategyType(SkipStrategy.class)
public interface BaseView extends MvpView {

    void bind();

    void showMessage(String message);

    void showAlertMessage(String title, String message);

    void onBackClicked();

    void addScreen(Screen screen, Object data);

    void addScreenForResult(Screen screen, int requestCode, Object... data);

    void showScreen(Screen screen);

    void closeScreen(String message);

    void closeScreenWithResult(int resultCode, String message);

    void showLoading();

    void hideLoading();

    void showFailed();

    void showError(Throwable error);
}
