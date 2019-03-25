package com.cryptocenter.andrey.owlsight.ui.screens.groups;

import android.util.Log;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.Camera;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.Screen;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

@InjectViewState
public class GroupsPresenter extends BasePresenter<GroupsView> {

    @Inject
    OwlsightRepository repository;

    private List<Group> groups;
    private boolean isMonitorMode = false;
    private String refreshingName = "";

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        fetchGroups();
        fetchMonitors();
    }

    void handleMenuClick() {
        getViewState().addScreen(Screen.MENU, null);
    }

    void handleTitleClick(int position) {
        getViewState().showAlertGroupEdit(position, groups.get(position).getGroupName());
    }

    void handlePageSelected(int position) {
        getViewState().setGroupTitle(groups.get((groups.size() == 1 && position == 1) ? 0 : position).getGroupName());
    }

    void handleOkEditGroupClick(int position, String title) {
        if (position == 0) return;
        editGroup(groups.get(position).getId(), title);
    }

    void handleMonitorsModeClick() {
        fetchMonitors();
//        if(!isMonitorMode) {
//            fetchMonitors();
//            isMonitorMode = true;
//        } else {
//            getViewState().hideScreens();
//            isMonitorMode = false;
//        }
    }

    void handleStreamModeClick() {
        getViewState().addScreen(Screen.STREAM, null);
    }

    void drawerclosed() {
        isMonitorMode = false;
    }

    void handleMonitorSelected(Monitor monitor) {
        getViewState().addScreen(Screen.MONITOR, monitor);
    }

    void fetchGroupsRefreshP(String name) {
        refreshingName = name;
        fetchGroupsRefresh();
    }

    //==============================================================================================
    // API
    //==============================================================================================

    private void fetchGroups() {
        repository.getGroupsCameras(
                getViewState()::showLoading,
                this::proceedGroupsSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void fetchGroupsRefresh() {
        repository.getGroupsCameras(
                getViewState()::showLoading,
                this::proceedGroupsRefreshSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void editGroup(String id, String title) {
        repository.editGroup(
                id, title,
                getViewState()::showLoading,
                result -> proceedEditGroupsSuccess(id, title),
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void fetchMonitors() {
        repository.getMonitors(
                getViewState()::showLoading,
                this::proceedMonitorsSuccess,
                this::onFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void onFailed() {
        proceedMonitorsSuccess(new ArrayList<>());
        Log.d("TAG", "Error on fetch monitors");
    }
    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedMonitorsSuccess(List<Monitor> monitors) {
        getViewState().showAlertMonitors(monitors);
    }

    private void proceedGroupsSuccess(List<Group> groups) {
        final List<Group> groupsSorted = new ArrayList<>();
        for (Group group : groups) {
            if (group.getCams() != null) {
                for (Camera camera : group.getCams()) {
                    if (camera.getHasRecordings() == null) {
                        camera.setGroupName(group.getGroupName());
                        camera.setGroupId(group.getId());
                        camera.setIsReachable(camera.getIsReachable());
                    } else {
                        camera.setGroupName(group.getGroupName());
                        camera.setGroupId(group.getId());
                    }
                }
            } else {
                group.setCams(new ArrayList<>());
//                group.getCams().add(Camera.getInstanceDelete(group));
            }
            groupsSorted.add(group);
        }
        groupsSorted.add(0, Group.instanceAddGroup());
        this.groups = groupsSorted;
        getViewState().setGroups(this.groups);
    }

    private void proceedGroupsRefreshSuccess(List<Group> groups) {
        final List<Group> groupsSorted = new ArrayList<>();

        for (Group group : groups) {
            if (group.getCams() != null) {
                for (Camera camera : group.getCams()) {
                    if (camera.getHasRecordings() == null) {
                        camera.setGroupName(group.getGroupName());
                        camera.setGroupId(group.getId());
                        camera.setIsReachable(camera.getIsReachable());
                    } else {
                        camera.setGroupName(group.getGroupName());
                        camera.setGroupId(group.getId());
                    }
                }
            } else {
                group.setCams(new ArrayList<>());
//                group.getCams().add(Camera.getInstanceDelete(group));
            }

            groupsSorted.add(group);
        }

        groupsSorted.add(0, Group.instanceAddGroup());
        this.groups = groupsSorted;
        getViewState().setGroupsRefreshed(this.groups, refreshingName);
    }

    public void onCameraAdded(Integer groupId) {
        Group group = findGroupById(groupId);
        if (group != null) {
            fetchGroupsRefreshP(group.getGroupName());
        }
        fetchGroups();
        fetchMonitors();

    }

    @Nullable
    private Group findGroupById(Integer groupId) {
        return Stream.ofNullable(groups)
                .filter(group -> group.getId().equals(groupId.toString()))
                .findFirst()
                .or(null)
                .get();
    }

    private void proceedEditGroupsSuccess(String id, String title) {
        for (Group group : groups) {
            if (group.getId().equals(id)) group.setGroupName(title);
        }
        getViewState().setGroupTitle(title);
        getViewState().showMessage(App.getInstance().getString(R.string.group_name_edited));
        getViewState().restart();
    }
}
