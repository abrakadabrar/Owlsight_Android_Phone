package com.cryptocenter.andrey.owlsight.ui.screens.monitor.adapter;

import android.view.View;
import android.widget.ProgressBar;

import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;
import com.cryptocenter.andrey.owlsight.managers.SinglePlayerManager;
import com.google.android.exoplayer2.ui.PlayerView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class MonitorCameraVH extends RecyclerView.ViewHolder {

    @BindView(R.id.playerView)
    PlayerView playerView;

    @BindView(R.id.progress)
    ProgressBar progress;

    private View itemView;
    private MonitorAdapter.OnMonitorCameraListener monitorCameraListener;
    public String id;

    MonitorCameraVH(final View itemView, MonitorAdapter.OnMonitorCameraListener monitorCameraListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
        this.monitorCameraListener = monitorCameraListener;
    }

    void setMonitorCamera(MonitorCamera monitorCamera) {
        progress.setVisibility(View.VISIBLE);
        id = monitorCamera.getId();
        itemView.setOnClickListener(v -> monitorCameraListener.onMonitorClick(monitorCamera));

        if (monitorCamera.hasRequestInfo() || monitorCamera.isRunning()) {
            progress.setVisibility(View.GONE);
            final SinglePlayerManager playerManager = new SinglePlayerManager(itemView.getContext(), id);
            playerManager.setVolume(0);
            playerView.setPlayer(playerManager.getPlayer());
        } else {
            monitorCameraListener.onMonitorGetInfo(monitorCamera);
        }
    }
}
