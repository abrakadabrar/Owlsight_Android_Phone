package com.cryptocenter.andrey.owlsight.data.model.videofromdatewithmotion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnail {

    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("hd")
    @Expose
    private String hd;
    @SerializedName("high")
    @Expose
    private String high;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("low")
    @Expose
    private String low;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

}