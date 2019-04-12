package com.cryptocenter.andrey.owlsight.data.event;

public class DeleteCameraEvent {

    private final String cameraId;

    public DeleteCameraEvent(String cameraId) {

        this.cameraId = cameraId;
    }

    public String getCameraId() {
        return cameraId;
    }
}
