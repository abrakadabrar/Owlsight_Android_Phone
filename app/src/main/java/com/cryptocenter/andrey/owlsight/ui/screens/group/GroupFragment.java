package com.cryptocenter.andrey.owlsight.ui.screens.group;

import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseFragment;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.screens.group.adapter.GroupAdapter;
import com.cryptocenter.andrey.owlsight.utils.Alerts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class GroupFragment extends BaseFragment implements GroupView, SwipeRefreshLayout.OnRefreshListener, GroupAdapter.OnCameraListener {

    @InjectPresenter
    GroupPresenter presenter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private GroupAdapter adapter;

    public static GroupFragment instance(List<Camera> group) {
        final Bundle arguments = new Bundle();
        arguments.putSerializable("group", (Serializable) group);
        final GroupFragment groupFragment = new GroupFragment();
        groupFragment.setArguments(arguments);
        return groupFragment;
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
        refreshLayout.setRefreshing(false);
    }


    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setGroup(List<Camera> cameras) {
        adapter = new GroupAdapter(cameras, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCameraClick(Camera camera) {
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
    public void showWarningDeleteGroup(Camera camera) {
        Alerts.showAlertWarningDeleteDialog(getContext(), () -> presenter.handleWarningDeleteOkClick(camera));
    }

    @Override
    public void showAlertCalendar(Camera camera) {
        Alerts.showDarkCalendar(camera, date -> presenter.handleDateSelect(camera, date), getChildFragmentManager());
    }

    @Override
    public void setCameraThumbnail(Camera camera) {
        adapter.setCamera(camera);
    }

    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        refreshLayout.setOnRefreshListener(this);

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
        return new GroupPresenter(repository, (ArrayList<Camera>) getArguments().getSerializable("group"));
    }
}
