package com.cryptocenter.andrey.owlsight.data.model.monitor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Monitor implements Serializable {

    @SerializedName("name")
    @Expose
    private String viewName;

    @SerializedName("cams")
    @Expose
    private List<MonitorCamera> cams = new ArrayList<>();

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public List<MonitorCamera> getCams() {
        return cams;
    }

    public void setCams(List<MonitorCamera> monitorCameras) {
        this.cams = monitorCameras;
    }

}
