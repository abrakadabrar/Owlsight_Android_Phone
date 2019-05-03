package com.cryptocenter.andrey.owlsight.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFERENCES_NAME = "autohelp-preferences";
    private static final String PREFERENCE_COOKIE = PREFERENCES_NAME + "-cookie";
    private static final String PREFERENCE_LOGIN = PREFERENCES_NAME + "-login";
    private static final String PREFERENCE_PASSWORD = PREFERENCES_NAME + "-password";
    private static final String PREFERENCE_FINGER = PREFERENCES_NAME + "-finger";
    private static final String PREFERENCES_NOTIFICATION_TOKEN = PREFERENCES_NAME + "-token";

    private SharedPreferences preferences;

    public Preferences(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveCookie(String cookie) {
        preferences.edit().putString(PREFERENCE_COOKIE, cookie).apply();
    }

    public String getCookie() {
        return preferences.getString(PREFERENCE_COOKIE, "");
    }

    public void saveLoginData(String login, String password) {
        preferences.edit().putString(PREFERENCE_LOGIN, login).putString(PREFERENCE_PASSWORD, password).apply();
    }

    public boolean isLogin() {
        return !preferences.getString(PREFERENCE_LOGIN, "").equalsIgnoreCase("");
    }

    public String[] getLoginData() {
        String[] data = new String[2];
        data[0] = preferences.getString(PREFERENCE_LOGIN, "");
        data[1] = preferences.getString(PREFERENCE_PASSWORD, "");
        return data;
    }

    public void setFingerAuth(boolean isFinger) {
        preferences.edit().putBoolean(PREFERENCE_FINGER, isFinger).apply();
    }

    public void setFirebaseNotificationToken(String token) {
        preferences.edit().putString(PREFERENCES_NOTIFICATION_TOKEN, token).apply();
    }

    public String getFirebaseNotificationToken() {
        return preferences.getString(PREFERENCES_NOTIFICATION_TOKEN, "");
    }

    public boolean isFingerAuth() {
        return preferences.getBoolean(PREFERENCE_FINGER, true);
    }

    public void cleanPreferences() {
        preferences.edit().clear().apply();
    }
}
