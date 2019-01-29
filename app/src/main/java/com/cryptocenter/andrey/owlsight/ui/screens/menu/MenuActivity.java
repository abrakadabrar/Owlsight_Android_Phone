package com.cryptocenter.andrey.owlsight.ui.screens.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import toothpick.Toothpick;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MenuActivity extends BaseActivity implements MenuView, NavigationView.OnNavigationItemSelectedListener {

    @InjectPresenter
    MenuPresenter presenter;
    public static Intent intent(Context context) {
        return new Intent(context, MenuActivity.class);
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        setupUI();
    }


    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void showAlertMonitors(List<Monitor> monitors) {
        Alerts.showAlertMonitors(this, monitors, presenter::handleMonitorSelected);
    }


    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        findViewById(R.id.btnClose).setOnClickListener(v -> presenter.handleCloseClick());
        findViewById(R.id.btn_monitor_mode).setOnClickListener(v -> presenter.handleMonitorsModeClick());
        findViewById(R.id.btn_camera_mode).setOnClickListener(v -> presenter.handleStreamModeClick());
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    MenuPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(MenuPresenter.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
