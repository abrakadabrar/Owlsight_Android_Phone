package com.cryptocenter.andrey.owlsight;

import android.app.Application;

import com.cryptocenter.andrey.owlsight.di.DependencyInjection;

import es.dmoral.toasty.Toasty;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Toasty.Config.getInstance().apply();
        DependencyInjection.configure(this);
    }
}
