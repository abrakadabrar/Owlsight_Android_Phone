package com.cryptocenter.andrey.owlsight.ui.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cryptocenter.andrey.owlsight.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CameraLoadingDialog extends DialogFragment {

    @BindView(R.id.btn_cancel_camera_loading_dialog)
    TextView btnCancel;

    private Context context;
    private Unbinder mUnbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_camera_loading,null,false);
        mUnbinder = ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        return builder.create();
    }

    @OnClick({R.id.btn_cancel_camera_loading_dialog})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_cancel_camera_loading_dialog:
                dismiss();
                break;
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            //TODO nothing
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }
}
