package com.cryptocenter.andrey.owlsight.ui.screens.launcher;

import android.os.Bundle;
import android.view.Window;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;

import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import toothpick.Toothpick;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class LauncherActivity extends BaseActivity implements LauncherView {

    @InjectPresenter
    LauncherPresenter presenter;


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    LauncherPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(LauncherPresenter.class);
    }
}
