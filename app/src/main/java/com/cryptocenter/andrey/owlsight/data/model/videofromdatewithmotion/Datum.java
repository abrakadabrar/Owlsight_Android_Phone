package com.cryptocenter.andrey.owlsight.data.model.videofromdatewithmotion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Datum {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("size")
    @Expose
    private Integer size;

    @SerializedName("startTime")
    @Expose
    private Integer startTime;

    @SerializedName("duration")
    @Expose
    private Double duration;

    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;

    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;

    @SerializedName("datetime")
    @Expose
    private String datetime;

    @SerializedName("start")
    @Expose
    private String start;

    @SerializedName("end")
    @Expose
    private String end;

    @SerializedName("hasMotion")
    @Expose
    private String hasMotion;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public Double getDuration() {
        return duration;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public String getDatetime() {
        return datetime;
    }

    public DateTime getStartDate() {
        return FORMATTER.parseDateTime(start);
    }

    public String getStart() {
        return start;
    }

    public DateTime getEndDate() {
        return FORMATTER.parseDateTime(end);
    }

    public String getEnd() {
        return end;
    }

    public String getHasMotion() {
        return hasMotion;
    }

}
