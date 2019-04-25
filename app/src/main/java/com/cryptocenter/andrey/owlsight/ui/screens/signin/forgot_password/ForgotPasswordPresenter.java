package com.cryptocenter.andrey.owlsight.ui.screens.signin.forgot_password;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Validator;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordView> {

    @Inject
    OwlsightRepository owlsightRepository;

    private Disposable fetchPasswordDisposable;

    void handleButtonSendNewPasswordClicked(String email) {
        if (Validator.isEmptyField(email)) {
            getViewState().showMessage(App.getInstance().getString(R.string.enter_a_valid_email_address));
        } else if (Validator.isNotValidEmail(email)) {
            getViewState().showMessage(App.getInstance().getString(R.string.enter_a_valid_email_address));
        } else {
            fetchPasswordReset(email);
        }
    }

    private void fetchPasswordReset(String email) {
        fetchPasswordDisposable = owlsightRepository.resetPassword(email)
                .doOnSubscribe(disposable -> getViewState().showLoading())
                .doOnComplete(() -> getViewState().hideLoading())
                .subscribe(jsonObjectResponse -> {
                    if (jsonObjectResponse.isSuccessful()) {
                        JsonObject body = jsonObjectResponse.body();
                        if (body != null) {
                            boolean success = body.get("success").getAsBoolean();
                            if(success) {
                                String message = body.get("message").getAsString();
                                getViewState().showMessage(message);
                                getViewState().popBackStack();
                            }
                        }
                    }

                }, error -> getViewState().showError(error));
    }

    @Override
    public void onDestroy() {
        if (fetchPasswordDisposable != null) {
            fetchPasswordDisposable.dispose();
        }
    }

    void handleButtonAuthorizationClicked() {
        getViewState().popBackStack();
    }
}
