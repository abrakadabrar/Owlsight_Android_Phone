package com.cryptocenter.andrey.owlsight.notification;

import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationBody {

    @SerializedName("cameraId")
    private String cameraId;
    @SerializedName("type")
    private String type;
    @SerializedName("aps")
    private Aps aps;

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public NotificationType getType() {
        return NotificationType.byStringRepresentation(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public Aps getAps() {
        return aps;
    }

    public void setAps(Aps aps) {
        this.aps = aps;
    }

    public String getCameraName(){
        return aps.getAlert().getLocArgs().get(0);
    }

    public static class Aps {
        @SerializedName("sound")
        private String sound;
        @SerializedName("alert")
        private Alert alert;

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public Alert getAlert() {
            return alert;
        }

        public void setAlert(Alert alert) {
            this.alert = alert;
        }
    }

    public static class Alert {
        @SerializedName("loc-args")
        private List<String> locArgs;
        @SerializedName("loc-key")
        private String locKey;
        @SerializedName("body")
        private String body;

        public List<String> getLocArgs() {
            return locArgs;
        }

        public void setLocArgs(List<String> locArgs) {
            this.locArgs = locArgs;
        }

        public String getLocKey() {
            return locKey;
        }

        public void setLocKey(String locKey) {
            this.locKey = locKey;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    public enum NotificationType {
        MOTION_DETECTED("motionDetection"),
        CAMERA_UNAVAILABLE("cameraUnavailable"),
        CAMERA_AVAILABLE("cameraAvailable");

        private final String stringRepresentation;

        NotificationType(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        public static NotificationType byStringRepresentation(String stringRepresentation) {
            return Stream.ofNullable(NotificationType.values())
                    .filter(value -> value.stringRepresentation.equals(stringRepresentation))
                    .findFirst()
                    .orElse(MOTION_DETECTED);
        }
    }
}
