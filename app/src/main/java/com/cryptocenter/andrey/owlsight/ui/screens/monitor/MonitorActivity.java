package com.cryptocenter.andrey.owlsight.ui.screens.monitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.screens.monitor.adapter.MonitorAdapter;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MonitorActivity extends BaseActivity implements MonitorView {

    @InjectPresenter
    MonitorPresenter presenter;
    @BindView(R.id.rvMonitor)
    RecyclerView rvMonitor;
    private MonitorAdapter adapter;

    public static Intent intent(Context context, Monitor monitor) {
        final Intent intent = new Intent(context, MonitorActivity.class);
        intent.putExtra("monitor", monitor);
        return intent;
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitor_view);
        ButterKnife.bind(this);
        setupUI();
    }


    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setMonitors(List<MonitorCamera> monitors) {
        adapter = new MonitorAdapter(monitors, presenter);
        rvMonitor.setAdapter(adapter);
    }

    @Override
    public void setRunningMonitor(MonitorCamera camera) {
        adapter.setMonitorCamera(camera);
    }


    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        rvMonitor.setLayoutManager(new GridLayoutManager(MonitorActivity.this, 4));
        rvMonitor.setItemAnimator(new DefaultItemAnimator());
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    MonitorPresenter providePresenter() {
        return new MonitorPresenter(
                Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository.class),
                (Monitor) getIntent().getSerializableExtra("monitor"));
    }
}
