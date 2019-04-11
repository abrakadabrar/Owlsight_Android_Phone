package com.cryptocenter.andrey.owlsight.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;

public class Camera implements Serializable, Parcelable {


    @SerializedName("cameraName")
    @Expose
    private String cameraName;

    @SerializedName("cameraId")
    @Expose
    private String cameraId;

    @SerializedName("isRecording")
    @Expose
    private String isRecording;

    @SerializedName("isReachable")
    @Expose
    private String isReachable;

    @SerializedName("hasRecordings")
    @Expose
    private String hasRecordings;

    private String groupName;
    private String groupId;
    private int groupCount;
    private String thumbnailUrl = "";
    private List<String> folders;
    private boolean isRefreshing;

    public Camera(String groupName,
                  String groupId,
                  String cameraId,
                  String cameraName,
                  String isRecording,
                  String isReachable,
                  String hasRecordings,
                  int groupCount
    ) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.isRecording = isRecording;
        this.isReachable = isReachable;
        this.hasRecordings = hasRecordings;
        this.groupCount = groupCount;
        this.isRefreshing = false;
    }

    public static Camera getInstanceDelete(Group group) {
        return new Camera(group.getGroupName()
                , group.getId()
                , "0"
                , "Удалить группу"
                , "0"
                , "0"
                , "0"
                , 0);
    }

    public String getIsReachable() {
        return isReachable;
    }

    public void setIsReachable(String isReachable) {
        this.isReachable = isReachable;
    }

    public String getHasRecordings() {
        return hasRecordings;
    }

    public void setHasRecordings(String hasRecordings) {
        this.hasRecordings = hasRecordings;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(String isRecording) {
        this.isRecording = isRecording;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        else if (obj == null) return false;
        else return cameraId.equals(((Camera) obj).cameraId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cameraName);
        dest.writeString(this.cameraId);
        dest.writeString(this.isRecording);
        dest.writeString(this.isReachable);
        dest.writeString(this.hasRecordings);
        dest.writeString(this.groupName);
        dest.writeString(this.groupId);
        dest.writeInt(this.groupCount);
        dest.writeString(this.thumbnailUrl);
        dest.writeStringList(this.folders);
        dest.writeByte(this.isRefreshing ? (byte) 1 : (byte) 0);
    }

    protected Camera(Parcel in) {
        this.cameraName = in.readString();
        this.cameraId = in.readString();
        this.isRecording = in.readString();
        this.isReachable = in.readString();
        this.hasRecordings = in.readString();
        this.groupName = in.readString();
        this.groupId = in.readString();
        this.groupCount = in.readInt();
        this.thumbnailUrl = in.readString();
        this.folders = in.createStringArrayList();
        this.isRefreshing = in.readByte() != 0;
    }

    public static final Creator<Camera> CREATOR = new Creator<Camera>() {
        @Override
        public Camera createFromParcel(Parcel source) {
            return new Camera(source);
        }

        @Override
        public Camera[] newArray(int size) {
            return new Camera[size];
        }
    };
}
