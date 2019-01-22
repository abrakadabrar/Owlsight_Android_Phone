package com.cryptocenter.andrey.owlsight.ui.screens.group.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<CameraVH> {

    private OnCameraListener cameraListener;
    private List<Camera> list;

    public GroupAdapter(List<Camera> list, OnCameraListener cameraListener) {
        this.list = list;
        this.cameraListener = cameraListener;
    }

    @NonNull
    @Override
    public CameraVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CameraVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false), cameraListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CameraVH holder, int position) {
        holder.setCamera(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setCamera(Camera newCamera) {
        int index = 0;
        for (Camera camera : list) {
            if (camera.getCameraId().equals(newCamera.getCameraId())) {
                index = list.indexOf(camera);
                camera.setThumbnailUrl(newCamera.getThumbnailUrl());
            }
        }
        notifyItemChanged(index);
    }

    public interface OnCameraListener {
        void onCameraClick(Camera camera);

        void onCalendarClick(Camera camera);

        void onDeleteClick(Camera camera);

        void onThumbnailLoad(Camera camera);
    }
}
