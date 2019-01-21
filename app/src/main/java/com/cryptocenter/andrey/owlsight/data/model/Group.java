package com.cryptocenter.andrey.owlsight.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Group {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("groupName")
    @Expose
    private String groupName;

    @SerializedName("cams")
    @Expose
    private List<Camera> cams = new ArrayList<Camera>();

    public static Group instanceAddGroup() {
        final Group group = new Group();
        group.setId("-1");
        group.setGroupName("Добавить группу");
        return group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Camera> getCams() {
        return cams;
    }

    public void setCams(List<Camera> cams) {
        this.cams = cams;
    }

}
