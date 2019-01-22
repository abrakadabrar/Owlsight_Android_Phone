package com.cryptocenter.andrey.owlsight.data.model.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecordsResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("totalCount")
    @Expose
    private float totalCount;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private RecordsData data;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public float getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(float totalCount) {
        this.totalCount = totalCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RecordsData getData() {
        return data;
    }

    public void setData(RecordsData data) {
        this.data = data;
    }

    public List<String> getCalendarData() {
        final List<String> tmp = new ArrayList<>();

        for (int i = 0; data.getFolders().size() > i; i++) {
            tmp.add(data.getFolders().get(i).toString());
        }

        return tmp;
    }

    private class RecordsData {

        @SerializedName("folders")
        @Expose
        private ArrayList<Object> folders = new ArrayList<>();

        public ArrayList<Object> getFolders() {
            return folders;
        }

        public void setFolders(ArrayList<Object> folders) {
            this.folders = folders;
        }
    }

}
