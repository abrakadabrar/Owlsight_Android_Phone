package com.cryptocenter.andrey.owlsight.data.model.records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnail {

    @SerializedName("original")
    @Expose
    private String original;

    public String getOriginal() { return this.original; }

    public void setOriginal(String original) { this.original = original; }

    @SerializedName("hd")
    @Expose
    private String hd;

    public String getHd() { return this.hd; }

    public void setHd(String hd) { this.hd = hd; }

    @SerializedName("high")
    @Expose
    private String high;

    public String getHigh() { return this.high; }

    public void setHigh(String high) { this.high = high; }

    @SerializedName("medium")
    @Expose
    private String medium;

    public String getMedium() { return this.medium; }

    public void setMedium(String medium) { this.medium = medium; }

    @SerializedName("low")
    @Expose
    private String low;

    public String getLow() { return this.low; }

    public void setLow(String low) { this.low = low; }
}


