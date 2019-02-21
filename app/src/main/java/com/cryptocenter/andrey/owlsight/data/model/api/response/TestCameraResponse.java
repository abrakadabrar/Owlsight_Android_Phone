package com.cryptocenter.andrey.owlsight.data.model.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TestCameraResponse implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("pinged")
    private boolean pinged;
    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("port")
    private int port;
    @SerializedName("message")
    private String message;
    @SerializedName("host")
    private String host;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isPinged() {
        return pinged;
    }

    public void setPinged(boolean pinged) {
        this.pinged = pinged;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeByte(this.pinged ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalCount);
        dest.writeInt(this.port);
        dest.writeString(this.message);
        dest.writeString(this.host);
    }

    public TestCameraResponse() {
    }

    protected TestCameraResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.pinged = in.readByte() != 0;
        this.totalCount = in.readInt();
        this.port = in.readInt();
        this.message = in.readString();
        this.host = in.readString();
    }

    public static final Parcelable.Creator<TestCameraResponse> CREATOR = new Parcelable.Creator<TestCameraResponse>() {
        @Override
        public TestCameraResponse createFromParcel(Parcel source) {
            return new TestCameraResponse(source);
        }

        @Override
        public TestCameraResponse[] newArray(int size) {
            return new TestCameraResponse[size];
        }
    };
}
