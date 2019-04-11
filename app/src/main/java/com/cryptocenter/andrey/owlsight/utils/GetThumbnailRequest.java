package com.cryptocenter.andrey.owlsight.utils;

import com.cryptocenter.andrey.owlsight.data.model.Camera;

public class GetThumbnailRequest {
    private final Camera camera;
    private final boolean isUpdated;

    public GetThumbnailRequest(Camera camera, boolean isUpdated) {

        this.camera = camera;
        this.isUpdated = isUpdated;
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean isUpdated() {
        return isUpdated;
    }
}
