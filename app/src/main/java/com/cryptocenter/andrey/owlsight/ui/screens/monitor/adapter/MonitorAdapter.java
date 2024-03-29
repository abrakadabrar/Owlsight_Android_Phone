package com.cryptocenter.andrey.owlsight.ui.screens.monitor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonitorAdapter extends RecyclerView.Adapter<MonitorCameraVH> {

    private OnMonitorCameraListener monitorCameraListener;
    private final int spanCount;
    private List<MonitorCamera> list;

    public MonitorAdapter(List<MonitorCamera> list, OnMonitorCameraListener monitorCameraListener, int spanCount) {
        this.list = list;
        this.monitorCameraListener = monitorCameraListener;
        this.spanCount = spanCount;
    }

    @NonNull
    @Override
    public MonitorCameraVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monitor, parent, false);
        final GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        lp.height = calculateHeight(parent.getMeasuredHeight());
        view.setLayoutParams(lp);
        return new MonitorCameraVH(view, monitorCameraListener);
    }

    private int calculateHeight(int measuredHeight) {
        switch (spanCount) {
            case 1:
                return measuredHeight;
            default:
            case 2:
            case 4:
                return measuredHeight / 2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MonitorCameraVH holder, int position) {
        holder.setMonitorCamera(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setMonitorCamera(MonitorCamera camera) {
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getId().equals(camera.getId())) {
                list.set(index, camera);
                notifyItemChanged(index);
            }
        }
    }

    public interface OnMonitorCameraListener {
        void onMonitorClick(MonitorCamera monitorCamera);

        void onMonitorGetInfo(MonitorCamera monitorCamera);
    }
}
