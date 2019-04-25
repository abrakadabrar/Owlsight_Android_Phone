package com.cryptocenter.andrey.owlsight.ui.screens.leave_profile;

import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseDialogFragment;

import butterknife.OnClick;

public class LeaveProfileDialogFragment extends BaseDialogFragment implements LeaveProfileView {
    public static LeaveProfileDialogFragment newInstance() {

        Bundle args = new Bundle();

        LeaveProfileDialogFragment fragment = new LeaveProfileDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter
    LeaveProfilePresenter presenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_leave_account;
    }

    @OnClick({R.id.btnYes, R.id.btnNo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnYes:
                presenter.handleButtonYesClicked();
                break;
            case R.id.btnNo:
                presenter.handleButtonNoClicked();
                break;
        }
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }
}
