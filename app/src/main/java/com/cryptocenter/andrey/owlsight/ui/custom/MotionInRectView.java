package com.cryptocenter.andrey.owlsight.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MotionInRectView extends View {

    private List<Rect> rectList = new ArrayList<>();
    Boolean flag = false;
    Paint paint = new Paint();
    Rect rect1 = new Rect();
    Rect rect2 = new Rect();
    Rect rect3 = new Rect();
    Rect rect4 = new Rect();
    Rect rect5 = new Rect();
    Rect rect6 = new Rect();
    Rect rect7 = new Rect();
    Rect rect8 = new Rect();
    Rect rect9 = new Rect();
    Rect rect10 = new Rect();
    Rect rect11 = new Rect();

    private void init() {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3f);
        paint.setStyle(Paint.Style.STROKE);
    }

    public MotionInRectView(Context context) {
        super(context);
        init();
    }

    public MotionInRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(flag) {
            drawThumb(canvas);
            rectList.clear();
        }else{
            drawThumb(canvas);
        }
    }

    public void setRect(List<Rect> rectList, Boolean flag){
        this.flag = flag;
        this.rectList.addAll(rectList);
    }

    private void drawThumb(Canvas canvas){

            switch (rectList.size()){
                case 1: rect1 = rectList.get(0);
                        canvas.drawRect(rect1, paint);
                break;
                case 2: rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                break;
                case 3: rect1 = rectList.get(0); rect2 = rectList.get(1); rect3 = rectList.get(2);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                break;
                case 4: rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                break;
                case 5: rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        rect5 = rectList.get(4);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                        canvas.drawRect(rect5, paint);
                break;
                case 6: rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        rect5 = rectList.get(4);
                        rect6 = rectList.get(5);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                        canvas.drawRect(rect5, paint);
                        canvas.drawRect(rect6, paint);
                break;
                case 7: rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        rect5 = rectList.get(4);
                        rect6 = rectList.get(5);
                        rect7 = rectList.get(6);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                        canvas.drawRect(rect5, paint);
                        canvas.drawRect(rect6, paint);
                        canvas.drawRect(rect7, paint);
                break;
                case 8: rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        rect5 = rectList.get(4);
                        rect6 = rectList.get(5);
                        rect7 = rectList.get(6);
                        rect8 = rectList.get(7);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                        canvas.drawRect(rect5, paint);
                        canvas.drawRect(rect6, paint);
                        canvas.drawRect(rect7, paint);
                        canvas.drawRect(rect8, paint);
                break;
                case 9: rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        rect5 = rectList.get(4);
                        rect6 = rectList.get(5);
                        rect7 = rectList.get(6);
                        rect8 = rectList.get(7);
                        rect9 = rectList.get(8);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                        canvas.drawRect(rect5, paint);
                        canvas.drawRect(rect6, paint);
                        canvas.drawRect(rect7, paint);
                        canvas.drawRect(rect8, paint);
                        canvas.drawRect(rect9, paint);
                break;
                case 10:rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        rect5 = rectList.get(4);
                        rect6 = rectList.get(5);
                        rect7 = rectList.get(6);
                        rect8 = rectList.get(7);
                        rect9 = rectList.get(8);
                        rect10 = rectList.get(9);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                        canvas.drawRect(rect5, paint);
                        canvas.drawRect(rect6, paint);
                        canvas.drawRect(rect7, paint);
                        canvas.drawRect(rect8, paint);
                        canvas.drawRect(rect9, paint);
                        canvas.drawRect(rect10, paint);
                break;
                case 11:rect1 = rectList.get(0);
                        rect2 = rectList.get(1);
                        rect3 = rectList.get(2);
                        rect4 = rectList.get(3);
                        rect5 = rectList.get(4);
                        rect6 = rectList.get(5);
                        rect7 = rectList.get(6);
                        rect8 = rectList.get(7);
                        rect9 = rectList.get(8);
                        rect10 = rectList.get(9);
                        rect11 = rectList.get(10);
                        canvas.drawRect(rect1, paint);
                        canvas.drawRect(rect2, paint);
                        canvas.drawRect(rect3, paint);
                        canvas.drawRect(rect4, paint);
                        canvas.drawRect(rect5, paint);
                        canvas.drawRect(rect6, paint);
                        canvas.drawRect(rect7, paint);
                        canvas.drawRect(rect8, paint);
                        canvas.drawRect(rect9, paint);
                        canvas.drawRect(rect10, paint);
                        canvas.drawRect(rect11, paint);
                break;
            }
    }
}

