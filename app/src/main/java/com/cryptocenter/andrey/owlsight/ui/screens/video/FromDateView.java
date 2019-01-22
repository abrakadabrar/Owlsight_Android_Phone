package com.cryptocenter.andrey.owlsight.ui.screens.video;

import android.graphics.Rect;
import android.net.Uri;

import com.cryptocenter.andrey.owlsight.base.BaseView;

import java.util.List;
import java.util.Map;

public interface FromDateView extends BaseView {
    void setFullscreenMode(boolean isEnable);
    void setVideoChanged(Uri url, Map<String, String> headers, int seekTo);
    void setRedLines(List<Integer> lines);
    void setTimeProgress(String progress);
    void setCurrentProgress(int progress);
    void setMotionRect(List<Rect> rectList);
}
