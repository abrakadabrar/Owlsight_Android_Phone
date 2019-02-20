package com.cryptocenter.andrey.owlsight.ui.screens.group.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class AddCameraVH extends RecyclerView.ViewHolder {

    public AddCameraVH(@NonNull View itemView, GroupAdapter.OnCameraListener cameraListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v->{if(cameraListener!=null){cameraListener.addCamera();}});
    }
}
