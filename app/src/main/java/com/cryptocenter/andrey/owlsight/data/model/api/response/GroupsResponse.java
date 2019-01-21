package com.cryptocenter.andrey.owlsight.data.model.api.response;

import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GroupsResponse {

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
    private List<Group> data = new ArrayList<Group>();

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

    public List<Group> getData() {
        return data;
    }

    public void setData(List<Group> data) {
        this.data = data;
    }

}
