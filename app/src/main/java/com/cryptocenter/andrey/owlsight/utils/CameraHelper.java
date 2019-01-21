package com.cryptocenter.andrey.owlsight.utils;

import android.content.Context;
import android.view.Surface;
import android.view.WindowManager;

public class CameraHelper {

    public static int getCameraOrientation(Context context) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0: //portrait
                return 90;
            case Surface.ROTATION_90: //landscape
                return 0;
            case Surface.ROTATION_180: //reverse portrait
                return 270;
            case Surface.ROTATION_270: //reverse landscape
                return 180;
            default:
                return 0;
        }
    }
}
