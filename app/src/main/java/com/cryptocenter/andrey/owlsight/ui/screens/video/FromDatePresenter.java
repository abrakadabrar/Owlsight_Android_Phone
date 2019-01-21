package com.cryptocenter.andrey.owlsight.ui.screens.video;

import android.graphics.Rect;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.data.FromDateData;
import com.cryptocenter.andrey.owlsight.data.model.motion.DatumFramesMotions;
import com.cryptocenter.andrey.owlsight.data.model.motion.Frame;
import com.cryptocenter.andrey.owlsight.data.model.videofromdatewithmotion.Datum;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@InjectViewState
public class FromDatePresenter extends BasePresenter<FromDateView> {

    private final String FILE_PATH = "https://owlsight.com/cabinet/record/get-file/?path=%s&id=%s";

    private OwlsightRepository repository;
    private Preferences preferences;

    private HashMap<Integer, DatumFramesMotions> motions = new HashMap<>();
    private Map<String, String> header = new HashMap<>();
    private Map<Integer, String> map = new HashMap<>();
    private List<Rect> rectList = new ArrayList<>();
    private List<Integer> paths = new ArrayList<>();

    private String cameraId;
    private int currPath = 0;
    private int tailTime = 0;
    private int seek = 0;

    private boolean requestComplete = false;
    private boolean isFullscreen = false;

    @Inject
    FromDatePresenter(OwlsightRepository repository, Preferences preferences) {
        this.repository = repository;
        header.put("Cookie", preferences.getCookie());
    }

    @Override
    public void showFailed() {
        getViewState().closeScreen("Ошибка соединения с сервером");
    }

    @Override
    public void handleBackClick() {
        if (!isFullscreen) {
            super.handleBackClick();
        } else {
            getViewState().setFullscreenMode(false);
        }
        isFullscreen = false;
    }

    void setCameraData(FromDateData data) {
        this.cameraId = data.getCameraId();
        fetchRecords(data.getDate());
    }

    void handleFullscreenClick() {
        isFullscreen = true;
        getViewState().setFullscreenMode(true);
    }

    void handleTimeLineSecondClick(int progress) {
        String path = "";
        if (progress > 600) {
            seek = progress % 600;
            for (int index = 0; index < paths.size(); index++) {
                if (paths.get(index).equals(progress / 600)) {
                    path = map.get(index);
                    currPath = progress / 600;
                }
            }
        }

        getViewState().setVideoChanged(Uri.parse(String.format(FILE_PATH, path, cameraId)), header, seek * 1000);
    }

    void handleVideoCompleted() {
        if (map.size() > currPath) currPath++;
        getViewState().setVideoChanged(Uri.parse(String.format(FILE_PATH, map.get(currPath), cameraId)), header, 0);
    }


    void handleTimerUpdate(boolean isPlaying, int current) {
        if (!requestComplete) return;

        if (isPlaying) {
            int seconds = currPath * 600 + current / 1000 + tailTime + 2;
            getViewState().setTimeProgress(TimeUtils.timeConv(seconds));
            getViewState().setCurrentProgress(currPath * 600 + current / 1000);
            rectList.clear();

            for (Integer second : motions.keySet()) {
                if (motions.get(seconds) != null && motions.get(seconds).getFrames() != null && second == seconds) {
                    for (Frame frame : motions.get(second).getFrames()) {
                        rectList.add(frame.toRect());
                    }

                    getViewState().setMotionRect(rectList);
                }
            }
        } else {
            getViewState().setMotionRect(rectList);
        }
    }

    //==============================================================================================
    // Api
    //==============================================================================================


    private void fetchRecords(String folder) {
        repository.records(
                cameraId,
                folder,
                getViewState()::showLoading,
                this::proceedRecordsSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }

    private void getFrameMotions(String from, String to) {
        repository.motions(
                cameraId,
                from,
                to,
                getViewState()::showLoading,
                this::proceedMotionsSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedRecordsSuccess(List<Datum> records) {
        for (int index = 0; index < records.size(); index++) {
            paths.add(index);
            map.put(index, records.get(index).getPath());
        }

        tailTime = TimeUtils.secondsFromHours(records.get(0).getStart());
        getFrameMotions(records.get(0).getStart(), records.get(records.size() - 1).getEnd());
        getViewState().setVideoChanged(Uri.parse(String.format(FILE_PATH, records.get(0).getPath(), cameraId)), header, 0);
    }

    private void proceedMotionsSuccess(List<DatumFramesMotions> frames) {
        final List<Integer> lines = new ArrayList<>();

        for (DatumFramesMotions framesMotions : frames) {
            lines.add(TimeUtils.secondsFromHours((framesMotions.getDate())));
            motions.put(TimeUtils.secondsFromHours(framesMotions.getDate()), framesMotions);
        }

        getViewState().setRedLines(lines);
        requestComplete = true;
    }
}
