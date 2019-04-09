package com.cryptocenter.andrey.owlsight.ui.screens.video;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.model.data.FromDateData;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.managers.ExoPlayerManager;
import com.cryptocenter.andrey.owlsight.managers.video_download.DownloadVideoManager;
import com.cryptocenter.andrey.owlsight.ui.custom.MotionInRectView;
import com.cryptocenter.andrey.owlsight.ui.custom.TimeLineView;
import com.cryptocenter.andrey.owlsight.ui.custom.VideoControlsTimelineView;
import com.cryptocenter.andrey.owlsight.utils.Permissions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Pair;
import toothpick.Toothpick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

public class FromDateActivity extends BaseActivity implements FromDateView, TimeLineView.TimeLineCallback {

    @InjectPresenter
    FromDatePresenter presenter;

    @BindView(R.id.rlVideo)
    RelativeLayout rlVideo;

    @BindView(R.id.viewTimeLine)
    TimeLineView viewTimeLine;

    @BindView(R.id.bottomSpace)
    View bottomSpace;

    @BindView(R.id.tvProgress)
    TextView tvProgress;

    @BindView(R.id.viewMotion)
    MotionInRectView viewMotion;

    @BindView(R.id.videoPlayerView)
    PlayerView videoPlayerView;

    @BindView(R.id.btnFullscreen)
    ImageButton btnFullscreen;

    @BindView(R.id.btnClose)
    ImageButton btnClose;

    @BindView(R.id.btnDownload)
    ImageButton btnDownload;

    @BindView(R.id.pbTimeLine)
    LinearLayout pbTimeLine;

    @BindView(R.id.exo_timeline_view)
    VideoControlsTimelineView videoControlsTimelineView;

    private Handler handler = new Handler();
    private Runnable runner;
    private Uri currentVideoUrl;
    private ExoPlayerManager exoPlayerManager;

    private List<Integer> redLines = new ArrayList<>();

    public static Intent intent(Context context, FromDateData data) {
        return new Intent(context, FromDateActivity.class)
                .putExtra("data", data);
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().addFlags(FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_date);
        ButterKnife.bind(this);
        setupUI();
        setupTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewTimeLine.setProgress(TimeLineView.SECS_IN_DAY / 2);
        if (redLines.size() > 0) {
            viewTimeLine.setRedLines(redLines);
        }
    }

    private void updateControlsTimeline() {
        Pair<Integer, Integer> currentInterval = presenter.getCurrentInterval();
        List<Integer> redSeconds =
                getRedSecondsByInterval(currentInterval.getFirst(), currentInterval.getSecond());
        videoControlsTimelineView.setIntervalData(redSeconds, currentInterval);
    }

    private List<Integer> getRedSecondsByInterval(int start, int end) {
        if (redLines == null || redLines.isEmpty()) {
            return Collections.emptyList();
        }
        Pair<Integer, Integer> startInterval = findBinaryInterval(redLines, start);
        Pair<Integer, Integer> endInterval = findBinaryInterval(redLines, end);
        if (startInterval == null || endInterval == null) {
            return Collections.emptyList();
        }
        int startInd = startInterval.getSecond();
        int endInd = endInterval.getFirst();
        if (startInd > endInd) {
            return Collections.emptyList();
        }
        return redLines.subList(startInd, endInd);
    }

    @Nullable
    private Pair<Integer, Integer> findBinaryInterval(List<Integer> values, int val) {
        if (values.isEmpty()) {
            return null;
        }
        int lowerInd = 0;
        int upperInd = values.size() - 1;
        while (upperInd - lowerInd > 1) {
            int middleInd = (upperInd + lowerInd) / 2;
            int curVal = values.get(middleInd);
            if (val == curVal) {
                return new Pair<>(middleInd, middleInd);
            } else if (val < curVal) {
                upperInd = middleInd;
            } else {
                lowerInd = middleInd;
            }
        }
        int lowerVal = values.get(lowerInd);
        int upperVal = values.get(upperInd);
        if (val == lowerVal) {
            return new Pair<>(lowerInd, lowerInd);
        } else if (val == upperVal) {
            return new Pair<>(upperInd, upperInd);
        } else {
            return new Pair<>(lowerInd, upperInd);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(runner);
        handler.removeCallbacks(runner);
    }

    @Override
    public void onBackClicked() {
        presenter.handleBackClick();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void setScale(float scale) {

    }

    @Override
    public void secondClicked(float progress) {
        presenter.handleTimeLineSecondClick((int) progress);
    }

    @Override
    public List<Integer> getRedLines() {
        return redLines;
    }

    @Override
    public void setRedLines(List<Integer> redLines) {
        this.redLines = redLines;
        viewTimeLine.setRedLines(redLines);
        viewTimeLine.setVisibility(GONE);
        viewTimeLine.setVisibility(VISIBLE);
    }

    @Override
    public void setFullscreenMode(boolean isEnable) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) bottomSpace.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mlp.bottomMargin,
                isEnable ? 0 : viewTimeLine.getHeight());
        valueAnimator.addUpdateListener(animation -> {
            mlp.bottomMargin = (int) animation.getAnimatedValue();
            bottomSpace.requestLayout();
        });
        valueAnimator.start();
        btnFullscreen.setVisibility(isEnable ? GONE : VISIBLE);
        btnDownload.setVisibility(isEnable ? VISIBLE : GONE);
        videoPlayerView.setUseController(isEnable);
    }

    @Override
    public void setVideoChanged(Uri url, Map<String, String> headers, int seekTo) {
        if (!url.equals(currentVideoUrl)) {
            updateControlsTimeline();
            exoPlayerManager.prepare(url);
            showLoading();
        }
        currentVideoUrl = url;
        if (seekTo != 0) {
            videoPlayerView.getPlayer().seekTo(seekTo);
        }
    }

    @Override
    public void setTimeProgress(String progress) {
        tvProgress.setText(progress);
    }

    @Override
    public void setCurrentProgress(int progress) {
        viewTimeLine.setProgress(progress);
        viewTimeLine.invalidate();
    }

    @Override
    public void setMotionRect(List<RectF> rectList) {
        viewMotion.setRect(rectList, exoPlayerManager.getExoPlayer().getPlaybackState() == Player.STATE_READY);
    }

    @Override
    public void setMinMaxTime(int minTimeSeconds, int maxTimeSeconds) {
        viewTimeLine.setMinMaxTimeSeconds(minTimeSeconds, maxTimeSeconds);
    }

    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        redLines.add(2);
        redLines.add(10);

        btnClose.setOnClickListener(v -> presenter.handleBackClick());
        btnFullscreen.setOnClickListener(v -> presenter.handleFullscreenClick());

        viewTimeLine.setISetScaleListener(this);
        viewTimeLine.callOnClick();

        presenter.setCameraData((FromDateData) getIntent().getSerializableExtra("data"));

        exoPlayerManager = new ExoPlayerManager(this);
        exoPlayerManager.setToView(videoPlayerView);
        exoPlayerManager.setOnLoadingChangedListener(isLoading -> {
            if (!isLoading) {
                hideLoading();
            }
        });
        exoPlayerManager.setOnPlayEndedListener(() -> presenter.handleVideoCompleted());
        // todo info?
//        videoPlayerView.setOnInfoListener((mp, what, extra) -> presenter.onVideoInfo(what));
    }

    private void setupTimer() {
        runner = () -> {
            handler.postDelayed(runner, 1000);
            ExoPlayer player = exoPlayerManager.getExoPlayer();
            boolean isPlaying = player.getPlaybackState() == Player.STATE_READY;
            presenter.handleTimerUpdate(isPlaying, (int) player.getCurrentPosition());
        };
        handler.postDelayed(runner, 1000);
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    FromDatePresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(FromDatePresenter.class);
    }

    @OnClick(R.id.btnDownload)
    public void onBtnDownloadClicked() {
        Permissions.checkStorage(this, isGranted -> {
            if (isGranted) {
                presenter.handleDownloadButtonClicked();
            } else {
                Toast.makeText(this, R.string.write_permission, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void downloadVideo(String path, String cameraId) {
        DownloadVideoManager.Companion.startService(this, path, cameraId);
    }

    @Override
    public void setVisibilityOfProgressBar(boolean visibility) {
        if (visibility) {
            pbTimeLine.setVisibility(VISIBLE);
        } else {
            pbTimeLine.setVisibility(GONE);
        }
    }
}
