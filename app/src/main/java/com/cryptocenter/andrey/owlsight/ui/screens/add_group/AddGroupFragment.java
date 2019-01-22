package com.cryptocenter.andrey.owlsight.ui.screens.add_group;

import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseFragment;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.utils.Alerts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import toothpick.Toothpick;

public class AddGroupFragment extends BaseFragment implements AddGroupView {

    @InjectPresenter
    AddGroupPresenter presenter;

    public static AddGroupFragment instance() {
        return new AddGroupFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_add_group;
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btnAddGroup).setOnClickListener(v -> presenter.handleAddGroupClick());
    }


    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void showAlertAddGroup() {
        Alerts.showGroupAddDialog(getActivity(), presenter::handleOkAddGroup);
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    AddGroupPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(AddGroupPresenter.class);
    }
}
