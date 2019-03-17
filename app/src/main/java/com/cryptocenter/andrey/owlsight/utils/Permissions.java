package com.cryptocenter.andrey.owlsight.utils;

import android.Manifest;
import android.app.Activity;

import com.cryptocenter.andrey.owlsight.utils.listeners.OnPermissionListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.fragment.app.FragmentActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

public class Permissions {

    public static void checkCamera(FragmentActivity context, OnPermissionListener listener) {
        new RxPermissions(context)
                .request(CAMERA, RECORD_AUDIO)
                .subscribe(listener::onGranted);
    }

    public static void checkStorage(Activity activity, OnPermissionListener listener) {
        new RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(listener::onGranted);
    }
}
