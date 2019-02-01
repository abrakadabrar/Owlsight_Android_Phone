package com.cryptocenter.andrey.owlsight.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import androidx.appcompat.widget.Toolbar;

public class DrawerUtil {
    public static Drawer getDrawer(final Activity activity, Toolbar toolbar, View contentView) {
        //create the drawer and remember the `Drawer` result object
        return new DrawerBuilder()
                .withDrawerGravity(Gravity.END)
                .withActivity(activity)
                .withCloseOnClick(true)
                .withSelectedItem(-1)
                .withCustomView(contentView)
                .build();
    }
}