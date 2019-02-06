package com.cryptocenter.andrey.owlsight.ui.screens.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseFragment;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.custom.CameraLoadingDialog;
import com.cryptocenter.andrey.owlsight.ui.screens.group.adapter.GroupAdapter;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.cryptocenter.andrey.owlsight.utils.Screen;
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
import me.dkzwm.widget.srl.MaterialSmoothRefreshLayout;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import toothpick.Toothpick;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class GroupFragment extends BaseFragment implements GroupView, SwipeRefreshLayout.OnRefreshListener, GroupAdapter.OnCameraListener {

    @InjectPresenter
    GroupPresenter presenter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    MaterialSmoothRefreshLayout refreshLayout;

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

    @Override
    public void startRefreshing() {
        refreshLayout.refreshComplete();
        adapter.startRefreshing();
    }

    @Override
    public void completeRefreshing() {
    // adapter.completeRefreshing();
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

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), isLandscape ? 2 : 2));
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
        alertDialog.dismiss();
//        dialog.dismiss();
    }
}
