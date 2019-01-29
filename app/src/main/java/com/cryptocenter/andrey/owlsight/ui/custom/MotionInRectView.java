package com.cryptocenter.andrey.owlsight.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cryptocenter.andrey.owlsight.data.model.motion.Frame;

import java.util.ArrayList;
import java.util.List;

public class MotionInRectView extends View {

    private List<RectF> rectList = new ArrayList<>();
    private boolean flag;
    Paint paint = new Paint();
    RectF rect1 = new RectF();
    RectF rect2 = new RectF();
    RectF rect3 = new RectF();
    RectF rect4 = new RectF();
    RectF rect5 = new RectF();
    RectF rect6 = new RectF();
    RectF rect7 = new RectF();
    RectF rect8 = new RectF();
    RectF rect9 = new RectF();
    RectF rect10 = new RectF();
    RectF rect11 = new RectF();

    private float dx;
    private float dy;
    private float scale = 1;

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
        if (flag) {
            drawThumb(canvas);
            rectList.clear();
        } else {
            drawThumb(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // recalculate dx, dy, scale for motion rectangle transform
        final float videoRatio = (float) Frame.WIDTH / Frame.HEIGHT;
        final float viewRatio = (float) getWidth() / getHeight();
        final float newHeight;
        final float newWidth;
        if (viewRatio > videoRatio) {
            // horizontally centered
            newHeight = getHeight();
            newWidth = videoRatio * newHeight;
            dy = 0;
            dx = (getWidth() - newWidth) / 2;
            scale = newWidth / Frame.WIDTH;
        } else {
            // vertically centered
            newWidth = getWidth();
            newHeight = newWidth / videoRatio;
            dx = 0;
            dy = (getHeight() - newHeight) / 2;
            scale = newWidth / Frame.WIDTH;
        }
    }

    public void setRect(List<RectF> rectList, boolean flag) {
        this.flag = flag;
        this.rectList.addAll(rectList);
        invalidate();
    }

    private void drawThumb(Canvas canvas) {
        transformRects(rectList);
        switch (rectList.size()) {
            case 1:
                rect1 = rectList.get(0);
                canvas.drawRect(rect1, paint);
                break;
            case 2:
                rect1 = rectList.get(0);
                rect2 = rectList.get(1);
                canvas.drawRect(rect1, paint);
                canvas.drawRect(rect2, paint);
                break;
            case 3:
                rect1 = rectList.get(0);
                rect2 = rectList.get(1);
                rect3 = rectList.get(2);
                canvas.drawRect(rect1, paint);
                canvas.drawRect(rect2, paint);
                canvas.drawRect(rect3, paint);
                break;
            case 4:
                rect1 = rectList.get(0);
                rect2 = rectList.get(1);
                rect3 = rectList.get(2);
                rect4 = rectList.get(3);
                canvas.drawRect(rect1, paint);
                canvas.drawRect(rect2, paint);
                canvas.drawRect(rect3, paint);
                canvas.drawRect(rect4, paint);
                break;
            case 5:
                rect1 = rectList.get(0);
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
            case 6:
                rect1 = rectList.get(0);
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
            case 7:
                rect1 = rectList.get(0);
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
            case 8:
                rect1 = rectList.get(0);
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
            case 9:
                rect1 = rectList.get(0);
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
            case 10:
                rect1 = rectList.get(0);
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
            case 11:
                rect1 = rectList.get(0);
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

    private void transformRects(List<RectF> rectList) {
        for (RectF rect : rectList) {
            rect.left = rect.left * scale + dx;
            rect.right = rect.right * scale + dx;
            rect.top = rect.top * scale + dy;
            rect.bottom = rect.bottom * scale + dy;
        }
    }
}
