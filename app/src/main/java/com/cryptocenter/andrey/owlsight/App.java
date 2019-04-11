package com.cryptocenter.andrey.owlsight;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.cryptocenter.andrey.owlsight.di.DependencyInjection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
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

    public static App getInstance() {
        return instance;
    }
}
