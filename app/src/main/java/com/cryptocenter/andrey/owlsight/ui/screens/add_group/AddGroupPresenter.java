package com.cryptocenter.andrey.owlsight.ui.screens.add_group;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import javax.inject.Inject;

@InjectViewState
public class AddGroupPresenter extends BasePresenter<AddGroupView> {

    @Inject
    OwlsightRepository repository;


    void handleAddGroupClick() {
        getViewState().showAlertAddGroup();
    }

    void handleOkAddGroup(String title) {
        addGroup(title);
    }


    //==============================================================================================
    // API
    //==============================================================================================

    private void addGroup(String title) {
        repository.addGroup(
                title,
                getViewState()::showLoading,
                this::proceedAddGroupsSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedAddGroupsSuccess(String title) {

        String group = App.getInstance().getString(R.string.group);
        String successfullyCreated = App.getInstance().getString(R.string.successfully_created);
        getViewState().showMessage(String.format("%s \"%s\", %s", group, title, successfullyCreated));
        getViewState().showScreen(Screen.GROUPS);
    }
}
