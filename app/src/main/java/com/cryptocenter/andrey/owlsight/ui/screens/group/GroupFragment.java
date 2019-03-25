package com.cryptocenter.andrey.owlsight.ui.screens.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseFragment;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.custom.CameraLoadingDialog;
import com.cryptocenter.andrey.owlsight.ui.custom.CustomCaldroidFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.group.adapter.GroupAdapter;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.cryptocenter.andrey.owlsight.utils.Screen;
import com.cryptocenter.andrey.owlsight.utils.listeners.OnAlertSelectDateListener;
import com.roomorama.caldroid.CaldroidListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.dkzwm.widget.srl.MaterialSmoothRefreshLayout;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import toothpick.Toothpick;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class GroupFragment extends BaseFragment implements GroupView, SwipeRefreshLayout.OnRefreshListener, GroupAdapter.OnCameraListener {
    private static final String GROUP_ID = "GROUP_ID";

    @InjectPresenter
    GroupPresenter presenter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    MaterialSmoothRefreshLayout refreshLayout;

    private GroupAdapter adapter;
    private IGroupsRefresh iGroupsRefresh;
    private String groupName;

    public static GroupFragment instance(List<Camera> group, IGroupsRefresh iGroupsRefresh, String groupName, String groupId) {
        final Bundle arguments = new Bundle();
        arguments.putString(GROUP_ID, groupId);
        arguments.putSerializable("group", (Serializable) group);
        final GroupFragment groupFragment = new GroupFragment();
        groupFragment.setArguments(arguments);
        groupFragment.iGroupsRefresh = iGroupsRefresh;
        groupFragment.groupName = groupName;
        return groupFragment;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroups(List<Camera> group) {
        adapter.setReachable(group);
        refreshLayout.refreshComplete();
        presenter.setCameras(group);


    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_group;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupUI();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addScreen(Screen screen, Object data) {
        hideLoading();
        super.addScreen(screen, data);
    }

    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setGroup(List<Camera> cameras) {
        adapter = new GroupAdapter(cameras, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCameraClick(Camera camera) {
        presenter.handleCameraResponce(true);
        presenter.setLoading(false);
        presenter.handleCameraClick(camera);
    }

    @Override
    public void onCalendarClick(Camera camera) {
        presenter.handleCalendarClick(camera);
    }

    @Override
    public void onDeleteClick(Camera camera) {
        presenter.handleDeleteGroupClick(camera);
    }

    @Override
    public void onThumbnailLoad(Camera camera) {
        presenter.handleGetThumbnail(camera);
    }

    @Override
    public void onThumbnailUploadLoad(Camera camera) {
        presenter.handleGetThumbnailUpload(camera);
    }

    @Override
    public void addCamera() {
        presenter.addCamera();
    }

    @Override
    public void showWarningDeleteGroup(Camera camera) {
        Alerts.showAlertWarningDeleteDialog(getContext(), () -> presenter.handleWarningDeleteOkClick(camera));
    }

    private CustomCaldroidFragment caldroidFragment;
    private CaldroidListener caldroidListener;
    private OnAlertSelectDateListener listener;

    @Override
    public void showAlertCalendar(Camera camera) {
//        Alerts.showDarkCalendar(camera, date -> presenter.handleDateSelect(camera, date), getChildFragmentManager());
        caldroidFragment = new CustomCaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CustomCaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CustomCaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CustomCaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        args.putInt(CustomCaldroidFragment.START_DAY_OF_WEEK, CustomCaldroidFragment.MONDAY);
        args.putBoolean(CustomCaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        args.putBoolean(CustomCaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
        caldroidFragment.setArguments(args);
        caldroidFragment.setThemeResource(R.style.CaldroidDefaultDark);

        Map<Date, Drawable> dates = new HashMap<>();
        final Map<Date, Integer> colors = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final ArrayList<Date> markedDays = new ArrayList<>();
        try {
            for (String date : camera.getFolders()) {
                markedDays.add(format.parse(date));
            }
        } catch (ParseException ignored) {

        }
        for (int i = 0; i < markedDays.size(); i++) {
            dates.put(markedDays.get(i), getResources().getDrawable(R.drawable.white_background_vector));
        }

        for (int i = 0; i < markedDays.size(); i++) {
            colors.put(markedDays.get(i), (R.color.color_bg_error));
        }
        caldroidFragment.setBackgroundDrawableForDates(dates);
        caldroidFragment.setTextColorForDates(colors);
        createCaldroidListener();
        caldroidFragment.setCaldroidListener(caldroidListener);
        caldroidFragment.show(getChildFragmentManager(), "caldroid");
        listener = date -> {
            try {
                if (markedDays.contains(format.parse(date))) {
                    presenter.handleDateSelect(camera, date);
                } else {
                    Toast.makeText(getContext(), R.string.records_doesnt_exists, Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

    }

    private void createCaldroidListener() {
        caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                dateSelected(date);
            }

            @Override
            public void onCaldroidViewCreated() {
                super.onCaldroidViewCreated();
                caldroidFragment.getWeekdayGridView().setBackground(getResources().getDrawable(R.drawable.week_background));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 30;
                params.topMargin = 30;
                params.leftMargin = 30;
                params.rightMargin = 30;
                caldroidFragment.getWeekdayGridView().setLayoutParams(params);
            }
        };
    }


    private void dateSelected(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        listener.onOkClick(format.format(date));
    }


    @Override
    public void setCameraThumbnail(Camera camera) {
        adapter.setCamera(camera);
    }

    @Override
    public void startRefreshing() {
        refreshLayout.refreshComplete();
        adapter.startRefreshing();
    }

    @Override
    public void completeRefreshing() {
//        adapter.completeRefreshing();
    }

    @Override
    public void refreshGroups() {
        iGroupsRefresh.refreshGroups(groupName);
    }

    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
//        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnRefreshListener(new SmoothRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                presenter.refresh();
            }

            @Override
            public void onLoadingMore() {

            }
        });
        final boolean isLandscape = getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), isLandscape ? 3 : 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    GroupPresenter providePresenter() {
        final OwlsightRepository repository = Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository.class);
        return new GroupPresenter(repository, (ArrayList<Camera>) getArguments().getSerializable("group"), getArguments().getString(GROUP_ID, ""));
    }


    private CameraLoadingDialog dialog;
    private AlertDialog alertDialog;

    @Override
    public void showLoading() {
//        dialog = new CameraLoadingDialog();
//        dialog.show(getChildFragmentManager(),"CameraLoadingDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.connecting_to_camera).setMessage(R.string.please_wait)
                .setView(new ProgressBar(getContext()))
                .setCancelable(true).setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                presenter.handleCameraResponce(false);
            }
        });
        alertDialog = builder.show();
    }

    @Override
    public void hideLoading() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
//        dialog.dismiss();
    }

    public interface IGroupsRefresh {
        void refreshGroups(String groupName);
    }
}
