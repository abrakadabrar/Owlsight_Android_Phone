package com.cryptocenter.andrey.owlsight.data.model.records;

import com.cryptocenter.andrey.owlsight.data.model.motion.DatumFramesMotions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MotionsRoot {
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
    private List<DatumFramesMotions> data = null;

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

    public List<DatumFramesMotions> getData() {
        return data;
    }

    public void setData(List<DatumFramesMotions> data) {
        this.data = data;
    }
}
