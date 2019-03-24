package com.cryptocenter.andrey.owlsight.ui.screens.video;

import android.graphics.RectF;
import android.net.Uri;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.cryptocenter.andrey.owlsight.base.BaseView;

import java.util.List;
import java.util.Map;

public interface FromDateView extends BaseView {
    void setFullscreenMode(boolean isEnable);

    void setVideoChanged(Uri url, Map<String, String> headers, int seekTo);

    void setRedLines(List<Integer> lines);

    void setTimeProgress(String progress);

    void setCurrentProgress(int progress);

    void setMotionRect(List<RectF> rectList);

    void setMinMaxTime(int minTimeSeconds, int maxTimeSeconds);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void downloadVideo(String path, String cameraId);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setVisibilityOfProgressBar(boolean visibility);
}