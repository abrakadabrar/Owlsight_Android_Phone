package com.cryptocenter.andrey.owlsight.utils;

import java.util.regex.Pattern;

public class Validator {

    private static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    public static boolean isEmptyFields(String email, String password) {
        return email.equals("") || email.length() == 0 || password.equals("") || password.length() == 0;
    }

    public static boolean isNotValidEmail(String email) {
        return !Pattern.compile(regEx).matcher(email).find();
    }

    public static boolean isNotValidName(String name) {
        String[] str = name.split(" ", name.length());
        return str.length != 2 || str[1].contains(" ");
    }

    public static boolean hasInvalidFields(String name, String email, String phone, String password, String confirmPassword, boolean isConfirmPolicy) {
        return name.equals("") || name.length() == 0
                || !isConfirmPolicy
                || email.equals("") || email.length() == 0
                || phone.equals("") || phone.length() == 0
                || password.equals("") || password.length() == 0
                || confirmPassword.equals("") || confirmPassword.length() == 0;
    }

    public static boolean isEmptyField(String field) {
        return field.equals("") || field.length() == 0;
    }
}
