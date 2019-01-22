package com.cryptocenter.andrey.owlsight.utils;

import android.os.Build;

public class ResourcesUtils {

    public static String getDeviceName() {
        final String model = Build.MODEL;
        if (model.startsWith(Build.MANUFACTURER)) {
            return capitalize(model);
        } else {
            return capitalize(Build.MANUFACTURER) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

}
