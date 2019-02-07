package com.cryptocenter.andrey.owlsight.ui.screens.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter.GroupsPagerAdapter;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter.MenuScreenAdapter;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.cryptocenter.andrey.owlsight.utils.DrawerUtil;
import com.mikepenz.materialdrawer.Drawer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

public class GroupsActivity extends BaseActivity implements GroupsView {

    @InjectPresenter
    GroupsPresenter presenter;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.add_group_toolbar)
    Toolbar toolbar;

    private Drawer drawer;
    private View menuView;
    private Switch swFinger;
    private Preferences preferences;
    private LinearLayout llMainContent;
    private LinearLayout llScreensContent;
    private RecyclerView rvMenuScreens;

    public static Intent intent(Context context) {
        return new Intent(context, GroupsActivity.class);
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new Preferences(this);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);
        setupUI();
        setupMenu();
        drawer = DrawerUtil.getDrawer(this, toolbar, menuView);
        drawer.getDrawerLayout().setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                hideScreens();
                presenter.drawerclosed();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setGroups(List<Group> groups) {
        presenter.handlePageSelected(1);
        pager.setAdapter(new GroupsPagerAdapter(getSupportFragmentManager(), groups));
        pager.setCurrentItem(groups.size() > 0 ? 1 : 0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                presenter.handlePageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void setGroupTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showAlertGroupEdit(int position, String oldTitle) {
        Alerts.showGroupEditDialog(this, oldTitle, newTitle -> presenter.handleOkEditGroupClick(position, newTitle));
    }

    @Override
    public void restart() {

    }

    @Override
    public void showAlertMonitors(List<Monitor> monitors) {
        llMainContent.setVisibility(View.GONE);
        llScreensContent.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvMenuScreens.setLayoutManager(manager);
        rvMenuScreens.setAdapter(new MenuScreenAdapter(monitors, m -> presenter.handleMonitorSelected(m)));
    }

    @Override
    public void hideScreens() {
        llMainContent.setVisibility(View.VISIBLE);
        llScreensContent.setVisibility(View.GONE);
    }

    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupMenu() {
        menuView = LayoutInflater.from(this).inflate(R.layout.drawer_latoyut, null, false);
//      menuView.findViewById(R.id.btnClose).setOnClickListener(v -> drawer.closeDrawer());
        menuView.findViewById(R.id.btn_monitor_mode).setOnClickListener(v -> presenter.handleMonitorsModeClick());
        menuView.findViewById(R.id.btn_camera_mode).setOnClickListener(v -> presenter.handleStreamModeClick());
        menuView.findViewById(R.id.btn_camera_mode).setOnClickListener(v -> presenter.handleStreamModeClick());
        llMainContent = menuView.findViewById(R.id.ll_menu_main_content);
        llScreensContent = menuView.findViewById(R.id.ll_menu_screens_content);
        swFinger = menuView.findViewById(R.id.sw_activity_menu_finger);
        rvMenuScreens = menuView.findViewById(R.id.rv_menu_rv_screens);
        swFinger.setChecked(preferences.isFingerAuth());
        swFinger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferences.setFingerAuth(isChecked);
            }
        });
    }

    private void setupUI() {
        findViewById(R.id.btnMenu).setOnClickListener(v -> openDrawer());
        tvTitle.setOnClickListener(v -> presenter.handleTitleClick(pager.getCurrentItem()));
    }

    private void openDrawer() {
        drawer.openDrawer();
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    GroupsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(GroupsPresenter.class);
    }
}
