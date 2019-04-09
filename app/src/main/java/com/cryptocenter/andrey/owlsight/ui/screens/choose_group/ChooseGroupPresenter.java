package com.cryptocenter.andrey.owlsight.ui.screens.choose_group;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class ChooseGroupPresenter extends BasePresenter<ChooseGroupView> {

    @Inject
    OwlsightRepository repository;

    private List<Group> groups;

    void onBtnClose() {

    }

    @Override
    protected void onFirstViewAttach() {
        fetchGroups();
    }

    void fetchGroups() {
        repository.getGroupsCameras(
                getViewState()::showLoading,
                this::proceedGroupsSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void proceedGroupsSuccess(List<Group> groups) {
        this.groups = groups;
        if (groups.isEmpty()) {
            getViewState().showMessage(App.getInstance().getString(R.string.please_add_group));
        } else if (groups.size() == 1) {
            getViewState().addCamera(Integer.parseInt(groups.get(0).getId()));
        } else {
            List<String> groupsName = Stream.ofNullable(groups)
                    .map(Group::getGroupName)
                    .toList();
            getViewState().showGroups(groupsName);
        }
    }

    public void onGroupSelected(String groupName) {
        Group findedGroup = Stream.ofNullable(groups)
                .filter(filterGroup -> filterGroup.getGroupName().equals(groupName))
                .findFirst()
                .orElse(null);
        if (findedGroup != null) {
            getViewState().addCamera(Integer.parseInt(findedGroup.getId()));
        } else {
            getViewState().showMessage(App.getInstance().getString(R.string.unable_to_find_group));
            getViewState().finishNoResult();
        }
    }
}
