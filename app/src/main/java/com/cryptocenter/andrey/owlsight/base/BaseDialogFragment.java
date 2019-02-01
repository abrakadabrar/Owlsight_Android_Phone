package com.cryptocenter.andrey.owlsight.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cryptocenter.andrey.owlsight.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment extends androidx.fragment.app.DialogFragment {

    private Unbinder mUnbinder;

    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            mActivity = activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(getLayout(), null, false);
        mUnbinder = ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    protected abstract int getLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }
}