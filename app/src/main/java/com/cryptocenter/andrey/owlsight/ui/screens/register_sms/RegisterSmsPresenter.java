package com.cryptocenter.andrey.owlsight.ui.screens.register_sms;

import android.app.Activity;
import android.os.CountDownTimer;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import javax.inject.Inject;

@InjectViewState
public class RegisterSmsPresenter extends BasePresenter<RegisterSmsView> {

    @Inject
    OwlsightRepository repository;

    private RegisterData registerData;
    private CountDownTimer timer;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        startSmsTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }

    void setRegisterData(RegisterData registerData) {
        this.registerData = registerData;
        getViewState().showPhoneLabel(String.format("Введите код, отправленный на номер +%s или в виде push:", registerData.getPhone()));
    }

    void handleCodeInput(String code) {
        if (code.length() == 4) registerSecondStep(code);
    }


    //==============================================================================================
    // API
    //==============================================================================================

    private void registerSecondStep(String code) {
        registerData.setConfirmSms(code);
        repository.registerSecondStep(registerData, code,
                getViewState()::showLoading,
                this::proceedRegisterSecondStepSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void startSmsTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long mills) {
                getViewState().showTimer(String.format("Код можно запросить повторно через %d секунд", mills / 1000));
            }

            public void onFinish() {
                getViewState().closeScreen("Время ожидания СМС кода истекло");
            }

        }.start();
    }

    private void proceedRegisterSecondStepSuccess(Void v) {
        getViewState().closeScreenWithResult(Activity.RESULT_OK, null);
    }
}
