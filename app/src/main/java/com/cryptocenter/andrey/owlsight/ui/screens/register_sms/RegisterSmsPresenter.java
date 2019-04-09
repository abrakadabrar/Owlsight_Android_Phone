package com.cryptocenter.andrey.owlsight.ui.screens.register_sms;

import android.os.CountDownTimer;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import javax.inject.Inject;

import static com.cryptocenter.andrey.owlsight.utils.Screen.GROUPS;

@InjectViewState
public class RegisterSmsPresenter extends BasePresenter<RegisterSmsView> {

    @Inject
    OwlsightRepository repository;

    @Inject
    Preferences preferences;

    private RegisterData registerData;
    private CountDownTimer timer;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        startSmsTimer();
    }

    @Override
    public void showFailed() {
        getViewState().showMessage(App.getInstance().getString(R.string.wrong_login_or_password));
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
        repository.login(registerData.getEmail(), registerData.getPassword(),
                getViewState()::showLoading,
                this::proceedLoginSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
//        getViewState().closeScreenWithResult(Activity.RESULT_OK, null);
    }

    private void proceedLoginSuccess(Void v) {
        preferences.saveLoginData(registerData.getEmail(), registerData.getPassword());
        getViewState().showScreen(GROUPS);
    }
}
