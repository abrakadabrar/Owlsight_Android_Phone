package com.cryptocenter.andrey.owlsight;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.cryptocenter.andrey.owlsight.di.DependencyInjection;

import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Toasty.Config.getInstance().apply();
        DependencyInjection.configure(this);
        initCrashlytics();
    }

    private void initCrashlytics() {
        CrashlyticsCore core = new CrashlyticsCore.Builder()
//                .disabled(BuildConfig.DEBUG)
                .build();

        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(core)
                .build();

        Fabric fabric = new Fabric
                .Builder(this)
                .debuggable(true)
                .kits(crashlytics).build();

        Fabric.with(fabric);

    }
}
