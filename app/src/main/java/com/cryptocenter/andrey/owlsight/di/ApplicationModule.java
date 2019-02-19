package com.cryptocenter.andrey.owlsight.di;

import android.content.Context;
import android.content.res.Resources;

import com.cryptocenter.andrey.owlsight.data.api.OwlsightAPI;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepositoryImpl;
import com.cryptocenter.andrey.owlsight.BuildConfig;
import com.google.gson.Gson;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import toothpick.config.Module;

class ApplicationModule extends Module {

    ApplicationModule(Context context) {
        this(context, null);
    }

    private ApplicationModule(Context context, String preferencesName) {
        final HttpLoggingInterceptor okHttpInterceptorLogging = new HttpLoggingInterceptor();
        okHttpInterceptorLogging.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new LoggingInterceptor.Builder()
                                .loggable(BuildConfig.DEBUG)
                                .setLevel(Level.BODY)
                                .log(Platform.INFO)
                                .tag("OkHttp")
                                .build()
                )
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        final OwlsightAPI api = new Retrofit.Builder()
                .baseUrl("https://owlsight.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build()
                .create(OwlsightAPI.class);
        final Preferences preferences = new Preferences(context);
        bind(OwlsightRepository.class).toInstance(new OwlsightRepositoryImpl(api, preferences));
        bind(Resources.class).toInstance(context.getResources());
        bind(Preferences.class).toInstance(preferences);
    }
}
