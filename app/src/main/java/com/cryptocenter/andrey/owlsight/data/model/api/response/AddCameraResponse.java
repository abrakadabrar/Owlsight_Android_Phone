package com.cryptocenter.andrey.owlsight.data.model.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddCameraResponse implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("totalCount")
    private int totalCount;
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalCount);
        dest.writeString(this.message);
    }

    public AddCameraResponse() {
    }

    protected AddCameraResponse(Parcel in) {
        this.success = in.readByte() != 0;
        this.totalCount = in.readInt();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<AddCameraResponse> CREATOR = new Parcelable.Creator<AddCameraResponse>() {
        @Override
        public AddCameraResponse createFromParcel(Parcel source) {
            return new AddCameraResponse(source);
        }

        @Override
        public AddCameraResponse[] newArray(int size) {
            return new AddCameraResponse[size];
        }
    };
}
