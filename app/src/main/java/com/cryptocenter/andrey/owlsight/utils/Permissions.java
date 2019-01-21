package com.cryptocenter.andrey.owlsight.utils;

import com.cryptocenter.andrey.owlsight.utils.listeners.OnPermissionStramListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.fragment.app.FragmentActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

public class Permissions {

    public static void checkCamera(FragmentActivity context, OnPermissionStramListener listener) {
        new RxPermissions(context)
                .request(CAMERA, RECORD_AUDIO)
                .subscribe(listener::onGranted);
    }
}
