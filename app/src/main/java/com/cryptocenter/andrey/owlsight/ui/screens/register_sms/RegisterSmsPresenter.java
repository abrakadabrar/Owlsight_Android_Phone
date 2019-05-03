package com.cryptocenter.andrey.owlsight.ui.screens.register_sms;

import android.os.CountDownTimer;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import java.util.Locale;

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
        getViewState().showPhoneLabel(String.format("%s %s %s:", App.getInstance().getString(R.string.enter_the_code_sent_to_the_number), registerData.getPhone(), App.getInstance().getString(R.string.or_in_the_form_of_push)));
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
                this::showFailedWithMessage,
                this::showError,
                getViewState()::hideLoading);
    }

    private void showFailedWithMessage(String error) {
        getViewState().showAlertMessage(App.getInstance().getString(R.string.errors_occurred_during_registration), error);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void startSmsTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long mills) {
                getViewState().showTimer(String.format(Locale.getDefault(),
                        "%s %d %s",
                        App.getInstance().getString(R.string.the_code_can_be_requested_again_after), mills / 1000, App.getInstance().getString(R.string.seconds)));
            }

            public void onFinish() {
                getViewState().closeScreen(App.getInstance().getString(R.string.sms_code_time_out));
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
