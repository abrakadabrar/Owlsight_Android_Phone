package com.cryptocenter.andrey.owlsight.ui.screens.group.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_ADD_CAMERA = 1;

    private OnCameraListener cameraListener;
    private List<Camera> list;

    public GroupAdapter(List<Camera> list, OnCameraListener cameraListener) {
        this.list = list;
        this.cameraListener = cameraListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_ADD_CAMERA:
                return new AddCameraVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_camera, parent, false));
        }
        return new CameraVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false), cameraListener);
    }

    public void startRefreshing(){
        for(Camera camera:list){
            camera.setRefreshing(true);
        }
        notifyDataSetChanged();
    }

    public void completeRefreshing(){
        for(Camera camera:list){
            camera.setRefreshing(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position!=list.size()) {
            ((CameraVH) holder).setCamera(list.get(position));
            if(list.get(position).isRefreshing()){
                ((CameraVH) holder).rlProgress.setVisibility(View.VISIBLE);
                //if(camera)
                ((CameraVH) holder).rlShadow.setVisibility(View.VISIBLE);
                ((CameraVH) holder).refreshCamera(list.get(position));
            } else {
                ((CameraVH) holder).rlProgress.setVisibility(View.GONE);
            }
        } else {

        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position!=list.size()){
            return TYPE_CAMERA;
        } else {
            return TYPE_ADD_CAMERA;
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public void setCamera(Camera newCamera) {
        int index = 0;
        for (Camera camera : list) {
            if (camera.getCameraId().equals(newCamera.getCameraId())) {
                index = list.indexOf(camera);
                camera.setThumbnailUrl(newCamera.getThumbnailUrl());
                camera.setRefreshing(false);
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
