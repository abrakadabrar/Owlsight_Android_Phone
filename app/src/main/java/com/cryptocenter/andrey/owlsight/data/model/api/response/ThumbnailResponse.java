package com.cryptocenter.andrey.owlsight.data.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThumbnailResponse {

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
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
