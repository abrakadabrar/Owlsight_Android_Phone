package com.cryptocenter.andrey.owlsight.ui.screens.register;

import android.app.Activity;
import android.text.Editable;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Screen;
import com.cryptocenter.andrey.owlsight.utils.Validator;

import javax.inject.Inject;

@InjectViewState
public class RegisterPresenter extends BasePresenter<RegisterView> {

    @Inject
    OwlsightRepository repository;

    void handleRegisterClick(String name, String email, String phone, String password, String confirmPassword, boolean isConfirmPolicy) {
        getViewState().hideFieldErrors();

        if (Validator.hasInvalidFields(name, email, phone, password, confirmPassword, isConfirmPolicy)) {
            if (Validator.isEmptyField(name)) {
                getViewState().showedFieldError(Field.NAME, true);
            }
            if (Validator.isEmptyField(email)) {
                getViewState().showedFieldError(Field.EMAIL, true);
            }
            if (Validator.isEmptyField(phone)) {
                getViewState().showedFieldError(Field.PHONE, true);
            }
            if (Validator.isEmptyField(password)) {
                getViewState().showedFieldError(Field.PASSWORD, true);
            }
            if (Validator.isEmptyField(confirmPassword)) {
                getViewState().showedFieldError(Field.CONFIRM_PASSWORD, true);
            }
            if (!isConfirmPolicy) {
                getViewState().showedFieldError(Field.CONFIRM_POLICY, true);
            }
//        } else if (Validator.isNotValidName(name)) {
//            getViewState().showMessage("Введите имя и фамилию");
        } else if (Validator.isNotValidEmail(email)) {
            getViewState().showMessage("Введите корректный адрес электронной почты");
        } else if (!confirmPassword.equals(password)) {
            getViewState().showMessage("Пароли не совпадают");
        } else {
            registerFirstStep(name, email, phone, password, confirmPassword);
        }
    }

    void handleFieldFocusChanged(Editable name, boolean hasFocus, Field field) {
        if (hasFocus) {
            getViewState().showedFieldError(field, false);
        } else if (Validator.isEmptyField(name.toString())) {
            getViewState().showedFieldError(field, true);
        }
    }

    void handleConfirmPolicyChanged(boolean isChecked) {
        getViewState().showedFieldError(Field.CONFIRM_POLICY, !isChecked);
    }

    void handleResult(int requestCode, int resultCode) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK)
            getViewState().closeScreen("Вы успешно зарегистрировались");
    }


    //==============================================================================================
    // API
    //==============================================================================================

    private void registerFirstStep(String name, String email, String phone, String password, String confirmPassword) {
        final RegisterData registerData = new RegisterData(name, email, phone, password, confirmPassword);
        repository.registerFirstStep(registerData,
                getViewState()::showLoading,
                v -> proceedRegisterFirstStepSuccess(registerData),
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedRegisterFirstStepSuccess(RegisterData registerData) {
        getViewState().addScreenForResult(Screen.REGISTER_SMS, 100, registerData);
    }


    enum Field {
        NAME, EMAIL, PHONE, PASSWORD, CONFIRM_PASSWORD, CONFIRM_POLICY
    }
}
