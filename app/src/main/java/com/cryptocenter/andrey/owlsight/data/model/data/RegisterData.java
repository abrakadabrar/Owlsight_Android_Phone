package com.cryptocenter.andrey.owlsight.data.model.data;

import java.io.Serializable;

public class RegisterData implements Serializable {

    private String name, email, phone, password, confirmPassword, confirmSms;

    public RegisterData(String name, String email, String phone, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmSms(String confirmSms) {
        this.confirmSms = confirmSms;
    }

    public String getConfirmSms() {
        return confirmSms;
    }
}
