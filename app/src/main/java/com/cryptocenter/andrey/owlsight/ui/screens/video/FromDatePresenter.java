package com.cryptocenter.andrey.owlsight.ui.screens.video;

import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.crashlytics.android.Crashlytics;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;
import com.cryptocenter.andrey.owlsight.data.model.data.FromDateData;
import com.cryptocenter.andrey.owlsight.data.model.motion.DatumFramesMotions;
import com.cryptocenter.andrey.owlsight.data.model.motion.Frame;
import com.cryptocenter.andrey.owlsight.data.model.videofromdatewithmotion.Datum;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class FromDatePresenter extends BasePresenter<FromDateView> {

    private final String FILE_PATH = "https://owlsight.com/cabinet/record/get-file/?path=%s&id=%s";

    private OwlsightRepository repository;

    private HashMap<Integer, DatumFramesMotions> motions = new HashMap<>();
    private Map<String, String> header = new HashMap<>();
    private List<Datum> records;
    private List<RectF> rectList = new ArrayList<>();

    private String cameraId;
    private int currentRecordInd;
    private int seek = 0;

    private boolean requestComplete = false;
    private boolean isFullscreen = false;

    private String path = "";

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
        for (int i = 0; i < records.size(); i++) {
            Datum datum = records.get(i);
            int startSecond = datum.getStartDate().getSecondOfDay();
            int endSecond = datum.getEndDate().getSecondOfDay();
            if (progress >= startSecond &&
                    progress <= endSecond) {
                currentRecordInd = i;
                path = datum.getPath();
                seek = progress - startSecond;
                break;
            }
        }

        getViewState().setVideoChanged(Uri.parse(String.format(FILE_PATH, path, cameraId)), header, seek * 1000);
    }

    void handleVideoCompleted() {
        if (currentRecordInd < records.size() - 1) {
            currentRecordInd++;
            getViewState().setVideoChanged(Uri.parse(String.format(FILE_PATH, records.get(currentRecordInd).getPath(), cameraId)), header, 0);
        }
    }

    private String convertTime(int seconds) {
        return LocalTime.fromMillisOfDay(seconds * 1_000).toString("HH : mm : ss");
    }

    void handleTimerUpdate(boolean isPlaying, int current) {
        try {


            if (!requestComplete) return;

            if (isPlaying) {
                Datum currentRecord = records.get(currentRecordInd);
                int seconds = currentRecord.getStartDate().getSecondOfDay() + current / 1000;
                getViewState().setTimeProgress(convertTime(seconds));
                getViewState().setCurrentProgress(seconds);
                rectList.clear();

                for (Integer second : motions.keySet()) {
                    if (motions.get(seconds) != null && motions.get(seconds).getFrames() != null && second == seconds) {
                        for (Frame frame : motions.get(second).getFrames()) {
                            if (frame != null) {
                                RectF rect = frame.toRect();
                                if (rect != null) {
                                    rectList.add(rect);
                                }
                            }
                        }

                        getViewState().setMotionRect(rectList);
                    }
                }
            } else {
                getViewState().setMotionRect(rectList);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
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

    private Disposable getFrameMotions(String from, String to) {
        return repository.motions(
                cameraId,
                from,
                to, () -> getViewState().setVisibilityOfProgressBar(true),
                this::proceedMotionsSuccess,
                this::showFailed,
                this::showError,
                getViewState()::hideLoading);
    }


    //==============================================================================================
    // Private
    //==============================================================================================

    private void proceedRecordsSuccess(List<Datum> records) {
        this.records = records;
        final Datum firstRecord = records.get(0);
        final Datum lastRecord = records.get(records.size() - 1);

        int minTimeSeconds = firstRecord.getStartDate().getSecondOfDay();
        int maxTimeSeconds = lastRecord.getStartDate().getSecondOfDay();
        getViewState().setMinMaxTime(minTimeSeconds, maxTimeSeconds);
        getFrameMotions(firstRecord.getStart(), lastRecord.getEnd());
        path = firstRecord.getPath();
        getViewState().setVideoChanged(Uri.parse(String.format(FILE_PATH, firstRecord.getPath(), cameraId)), header, 0);
    }

    private void proceedMotionsSuccess(List<DatumFramesMotions> frames) {
        final List<Integer> lines = new ArrayList<>();

        for (DatumFramesMotions framesMotions : frames) {
            int seconds = framesMotions.getDate().getSecondOfDay();
            lines.add(seconds);
            motions.put(seconds, framesMotions);
        }

        getViewState().setRedLines(lines);
        getViewState().setVisibilityOfProgressBar(false);
        requestComplete = true;
    }

    public void handleDownloadButtonClicked() {
        getViewState().downloadVideo(path, cameraId);
    }

    boolean onVideoInfo(int what) {
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
            getViewState().setVisibilityOfProgressBar(false);
            return true;
        } else return false;
    }
}