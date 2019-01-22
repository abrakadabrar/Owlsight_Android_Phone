package com.cryptocenter.andrey.owlsight.data.model.records;


import com.cryptocenter.andrey.owlsight.data.model.motion.Datum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RootObject
{
    @SerializedName("success")
    @Expose
    private boolean success;

    public boolean getSuccess() { return this.success; }

    public void setSuccess(boolean success) { this.success = success; }

    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    public int getTotalCount() { return this.totalCount; }

    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data;

    public ArrayList<Datum> getData() { return this.data; }

    public void setData(ArrayList<Datum> data) { this.data = data; }
}

