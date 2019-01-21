package com.cryptocenter.andrey.owlsight.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREFERENCES_NAME = "autohelp-preferences";
    private static final String PREFERENCE_COOKIE = PREFERENCES_NAME + "-cookie";

    private SharedPreferences preferences;

    public Preferences(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveCookie(String cookie) {
        preferences.edit().putString(PREFERENCE_COOKIE, cookie).commit();
    }

    public String getCookie() {
        return preferences.getString(PREFERENCE_COOKIE, "");
    }
}
