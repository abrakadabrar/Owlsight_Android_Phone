package com.cryptocenter.andrey.owlsight.data.model.api.response;

import com.cryptocenter.andrey.owlsight.data.model.monitor.MonitorCamera;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MonitorResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<MonitorCamera> data = new ArrayList<>();

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MonitorCamera> getData() {
        return data;
    }

    public void setData(List<MonitorCamera> data) {
        this.data = data;
    }
}
