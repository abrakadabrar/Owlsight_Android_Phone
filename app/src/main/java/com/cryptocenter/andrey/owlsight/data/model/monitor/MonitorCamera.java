package com.cryptocenter.andrey.owlsight.data.model.monitor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MonitorCamera implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("cameraName")
    @Expose
    private String cameraName;

    @SerializedName("streamLink")
    @Expose
    private String streamLink;

    @SerializedName("isRecording")
    @Expose
    private String isRecording;

    @SerializedName("hasRecordings")
    @Expose
    private Object hasRecordings;

    @SerializedName("shared")
    @Expose
    private String shared;

    @SerializedName("tz")
    @Expose
    private String tz;

    private boolean isRunning = false;
    private boolean hasRequestInfo = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getStreamLink() {
        return streamLink;
    }

    public void setStreamLink(String streamLink) {
        this.streamLink = streamLink;
    }

    public String getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(String isRecording) {
        this.isRecording = isRecording;
    }

    public Object getHasRecordings() {
        return hasRecordings;
    }

    public void setHasRecordings(Object hasRecordings) {
        this.hasRecordings = hasRecordings;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning() {
        return false;
    }

    public void setHasRequestInfo(boolean hasRequestInfo) {
        this.hasRequestInfo = hasRequestInfo;
    }

    public boolean hasRequestInfo() {
        return hasRequestInfo;
    }
}
