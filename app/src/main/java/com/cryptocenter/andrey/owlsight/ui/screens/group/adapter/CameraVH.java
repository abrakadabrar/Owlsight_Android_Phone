package com.cryptocenter.andrey.owlsight.ui.screens.group.adapter;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.signature.ObjectKey;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.utils.GetThumbnailRequest;
import com.cryptocenter.andrey.owlsight.utils.GlideApp;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraVH extends RecyclerView.ViewHolder {

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvWarningMessage)
    TextView tvWarningMessage;

    @BindView(R.id.ivPreview)
    ImageView ivPreview;

    @BindView(R.id.btnCalendar)
    ImageView btnCalendar;

    @BindView(R.id.viewRecording)
    ImageView viewRecording;

    @BindView(R.id.viewWarning)
    ImageView viewWarning;

    @BindView(R.id.btnDelGroup)
    Button btnDelGroup;

    @BindView(R.id.rl_item_camera_refresh)
    RelativeLayout rlProgress;

    @Nullable
    @BindView(R.id.rl_item_camera_shadow)
    RelativeLayout rlShadow;

    @BindView(R.id.btnOptions)
    AppCompatImageView btnOptions;

    private GroupAdapter.OnCameraListener cameraListener;
    private boolean isRefreshing = false;

    CameraVH(View itemView, GroupAdapter.OnCameraListener cameraListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.cameraListener = cameraListener;
    }

    public void refreshCamera(Camera camera) {
        isRefreshing = true;
        cameraListener.onThumbnailUploadLoad(camera);
    }

    public void setCamera(Camera camera) {
        tvName.setText(camera.getCameraName());

        if (camera.getIsRecording() != null && camera.getIsRecording().equals("1")) {
            btnCalendar.setVisibility(View.VISIBLE);
            viewRecording.setVisibility(View.VISIBLE);
        } else {
            viewRecording.setVisibility(View.GONE);
            btnCalendar.setVisibility(View.GONE);
        }

        if (camera.getIsReachable() != null && camera.getIsReachable().equals("1")) {
            tvWarningMessage.setVisibility(View.GONE);
            if (rlShadow != null) {
                rlShadow.setVisibility(View.GONE);
            }
            viewWarning.setVisibility(View.GONE);
        } else {
            tvWarningMessage.setVisibility(View.VISIBLE);
            viewWarning.setVisibility(View.VISIBLE);
            viewRecording.setVisibility(View.GONE);
            btnCalendar.setVisibility(View.GONE);
        }

        if (camera.getHasRecordings() != null && camera.getHasRecordings().equals("1") || (camera.getIsRecording() != null && camera.getIsRecording().equals("1"))) {
            btnCalendar.setVisibility(View.VISIBLE);
        } else {
            btnCalendar.setVisibility(View.GONE);
        }
        if (!camera.getCameraId().equals("0") && !camera.isRefreshing()) {
            rlProgress.setVisibility(View.GONE);
            if (ivPreview.getDrawable() == null || camera.getThumbnailUrl() == null || camera.getThumbnailUrl().isEmpty()) {
                cameraListener.onThumbnailLoad(camera);
            } else if (camera.getThumbnailUrl() != null && !camera.getThumbnailUrl().isEmpty()) {
                byte[] decodedBytes = Base64.decode(camera.getThumbnailUrl(), Base64.DEFAULT);
                ivPreview.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
                camera.setThumbnailUrl("");
            }
        } else {
            rlProgress.setVisibility(View.VISIBLE);
            cameraListener.onThumbnailUploadLoad(camera);
        }
        GlideApp.with(ivPreview)
                .load(new GetThumbnailRequest(camera, true))
                .into(ivPreview);

        if (camera.getIsReachable().equals("1")) {
            itemView.setOnClickListener(v -> cameraListener.onCameraClick(camera));
        }
        btnCalendar.setOnClickListener(v -> cameraListener.onCalendarClick(camera));
        btnDelGroup.setOnClickListener(v -> cameraListener.onDeleteClick(camera));
        btnOptions.setOnClickListener(v -> cameraListener.onOptionsClicked(Integer.parseInt(camera.getCameraId())));
    }
}
