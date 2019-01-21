package com.cryptocenter.andrey.owlsight.di;

import android.content.Context;

import toothpick.Toothpick;
import toothpick.configuration.Configuration;

public class DependencyInjection {

    public static void configure(Context context) {
        Toothpick.setConfiguration(getConfig());
        initScopes(context);
    }

    private static Configuration getConfig() {
        return Configuration.forDevelopment().preventMultipleRootScopes();
    }

    private static void initScopes(Context context) {
        Toothpick.openScope(Scopes.APP).installModules(new ApplicationModule(context));
    }
}
