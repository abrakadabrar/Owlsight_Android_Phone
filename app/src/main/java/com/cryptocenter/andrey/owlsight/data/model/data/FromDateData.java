package com.cryptocenter.andrey.owlsight.data.model.data;

import java.io.Serializable;

public class FromDateData implements Serializable {

    private String cameraId, date;

    public FromDateData(String cameraId, String date) {
        this.cameraId = cameraId;
        this.date = date;
    }

    public String getCameraId() {
        return cameraId;
    }

    public String getDate() {
        return date;
    }
}
