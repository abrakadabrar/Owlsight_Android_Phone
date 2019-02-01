package com.cryptocenter.andrey.owlsight.data.model.motion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class DatumFramesMotions  {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("frames")
    @Expose
    private List<Frame> frames = null;

    public DateTime getDate() {
        return FORMATTER.parseDateTime(date);
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