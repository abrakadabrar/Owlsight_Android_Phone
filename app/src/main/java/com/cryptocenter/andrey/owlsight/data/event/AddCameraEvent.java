package com.cryptocenter.andrey.owlsight.data.event;

public class AddCameraEvent {
    private final Integer groupId;

    public AddCameraEvent(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }
}
