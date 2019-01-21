package com.cryptocenter.andrey.owlsight.data.model.motion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DatumFramesMotions  {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("frames")
    @Expose
    private List<Frame> frames = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

}
