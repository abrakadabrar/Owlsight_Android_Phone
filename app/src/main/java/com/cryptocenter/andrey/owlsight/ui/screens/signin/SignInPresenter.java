package com.cryptocenter.andrey.owlsight.ui.screens.signin;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Screen;
import com.cryptocenter.andrey.owlsight.utils.Validator;

import javax.inject.Inject;

import static com.cryptocenter.andrey.owlsight.utils.Screen.GROUPS;

@InjectViewState
public class SignInPresenter extends BasePresenter<SignInView> {

    @Inject
    OwlsightRepository repository;

    @Override
    public void showFailed() {
        getViewState().showMessage(App.getInstance().getString(R.string.wrong_login_or_password));
    }

    void handleLoginClick(String email, String password) {
        if (Validator.isEmptyFields(email, password)) {
            getViewState().showMessage(App.getInstance().getString(R.string.all_fields_are_required));
        } else if (Validator.isNotValidEmail(email)) {
            getViewState().showMessage(App.getInstance().getString(R.string.enter_a_valid_email_address));
        } else {
            fetchLogin(email, password);
        }
    }

    void handleRegistrationClick() {
        getViewState().addScreen(Screen.REGISTER, null);
    }


    //==============================================================================================
    // API
    //==============================================================================================

    private void fetchLogin(String email, String password) {
        repository.login(email, password,
                getViewState()::showLoading,
                this::proceedLoginSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedLoginSuccess(Void v) {
        getViewState().saveLoginData();
        getViewState().showScreen(GROUPS);
    }
}
