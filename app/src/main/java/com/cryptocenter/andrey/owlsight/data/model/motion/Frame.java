package com.cryptocenter.andrey.owlsight.data.model.motion;

import android.graphics.RectF;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class Frame {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("x1")
    @Expose
    private Integer x1;
    @SerializedName("y1")
    @Expose
    private Integer y1;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getX1() {
        return x1;
    }

    public void setX1(Integer x1) {
        this.x1 = x1;
    }

    public Integer getY1() {
        return y1;
    }

    public void setY1(Integer y1) {
        this.y1 = y1;
    }

    @Nullable
    public RectF toRect() {
        if(x == null || x1 ==null || y == null || y1 == null){
            return null;
        }
        RectF rect = new RectF();
        rect.set(x, y, x1, y1);
        return rect;
    }
}