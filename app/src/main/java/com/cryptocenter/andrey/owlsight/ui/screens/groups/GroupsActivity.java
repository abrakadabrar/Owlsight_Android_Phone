package com.cryptocenter.andrey.owlsight.ui.screens.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.event.AddCameraEvent;
import com.cryptocenter.andrey.owlsight.data.event.DeleteCameraEvent;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.screens.group.GroupFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter.GroupsPagerAdapter;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter.MenuScreenAdapter;
import com.cryptocenter.andrey.owlsight.ui.screens.stream.StreamActivity;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.cryptocenter.andrey.owlsight.utils.DrawerUtil;
import com.mikepenz.materialdrawer.Drawer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

public class GroupsActivity extends BaseActivity implements GroupsView, GroupFragment.IGroupsRefresh {

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
    private AppCompatTextView tvNoSpecies;
    private GroupsPagerAdapter adapter;

    public static Intent intent(Context context) {
        Intent intent = new Intent(context, GroupsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
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

    @Override
    public void refreshGroups(String name) {
        presenter.fetchGroupsRefreshP(name);
    }

    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setGroups(List<Group> groups) {
        presenter.handlePageSelected(1);

        adapter = new GroupsPagerAdapter(getSupportFragmentManager(), this, groups);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
//        pager.post(() -> pager.setCurrentItem(1));
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
    public void setGroupsRefreshed(List<Group> groups, String refreshingName) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof GroupFragment) {
                if (((GroupFragment) fragment).getGroupName().equalsIgnoreCase(refreshingName)) {
                    if (getGroupByName(groups, ((GroupFragment) fragment).getGroupName()) != null) {
                        ((GroupFragment) fragment).setGroups(getGroupByName(groups, ((GroupFragment) fragment).getGroupName()).getCams());
                    }
                }
            }
        }
    }

    private Group getGroupByName(List<Group> groups, String name) {
        for (Group group : groups) {
            if (group.getGroupName().equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
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
//        llMainContent.setVisibility(View.GONE);
//        llScreensContent.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvMenuScreens.setLayoutManager(manager);
        if (!monitors.isEmpty()) {
            tvNoSpecies.setVisibility(View.GONE);
            rvMenuScreens.setVisibility(View.VISIBLE);
            rvMenuScreens.setAdapter(new MenuScreenAdapter(monitors, m -> presenter.handleMonitorSelected(m)));
        } else {
            rvMenuScreens.setVisibility(View.GONE);
            tvNoSpecies.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StreamActivity.STREAM_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> presenter.handleStreamRestart(),  3000);
        }
    }

    @Override
    public void hideScreens() {
        llScreensContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void proceedBtnCameraModeClicked() {
        Button btnCameraMode = menuView.findViewById(R.id.btn_camera_mode);
        btnCameraMode.performClick();
    }

    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupMenu() {
        menuView = LayoutInflater.from(this).inflate(R.layout.drawer_layout, null, false);
//        menuView.findViewById(R.id.btnClose).setOnClickListener(v -> drawer.closeDrawer());
        menuView.findViewById(R.id.btn_monitor_mode).setOnClickListener(v -> presenter.handleMonitorsModeClick());
        menuView.findViewById(R.id.btn_camera_mode).setOnClickListener(v -> presenter.handleStreamModeClick());
        menuView.findViewById(R.id.btn_camera_mode).setOnClickListener(v -> presenter.handleStreamModeClick());
        menuView.findViewById(R.id.btnProfile).setOnClickListener(v -> presenter.handleProfileButtonClicked());
        llMainContent = menuView.findViewById(R.id.ll_menu_main_content);
        llScreensContent = menuView.findViewById(R.id.ll_menu_screens_content);
        swFinger = menuView.findViewById(R.id.sw_activity_menu_finger);
        rvMenuScreens = menuView.findViewById(R.id.rv_menu_rv_screens);
        tvNoSpecies = menuView.findViewById(R.id.tvNoSpecies);
        rvMenuScreens.setNestedScrollingEnabled(false);
        preferences.setFingerAuth(true);
//        swFinger.setChecked(preferences.isFingerAuth());
//        swFinger.setOnCheckedChangeListener((buttonView, isChecked) -> preferences.setFingerAuth(isChecked));
    }

    private void setupUI() {
        findViewById(R.id.btnMenu).setOnClickListener(v -> openDrawer());
        tvTitle.setOnClickListener(v -> presenter.handleTitleClick(pager.getCurrentItem()));
    }

    private void openDrawer() {
        drawer.openDrawer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCameraAdded(AddCameraEvent event) {
        presenter.onCameraAdded(event.getGroupId());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    GroupsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(GroupsPresenter.class);
    }

}
