package com.cryptocenter.andrey.owlsight.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class SinglePlayerManager implements Player.EventListener {

    private Context context;
    private SimpleExoPlayer player;
    private String id;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean playWhenReady = true;
    private ProgressDialog progress;

    public SinglePlayerManager(Context context, String id) {
        this.context = context;
        this.id = id;
        progress = new ProgressDialog(context);
        progress.setCancelable(false);
        progress.setTitle("Загузка стрима");
        initPlayer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_READY:
                progress.dismiss();
                break;
            case ExoPlayer.STATE_BUFFERING:
                progress.dismiss();
                break;
        }
    }

    public void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    public void setVolume(float audioVolume){
        player.setVolume(audioVolume);
    }

    public Player getPlayer() {
        return player;
    }

    private void initPlayer() {
        final TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(adaptiveTrackSelection), new DefaultLoadControl());

        final Uri uri = Uri.parse(String.format("https://owlsight.com/cabinet/view/get-stream/%s/stream-low.m3u8", id));

        final DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(Util.getUserAgent(context, "Owlsight"));
        dataSource.getDefaultRequestProperties().set("Cookie", new Preferences(context).getCookie());
        dataSource.getDefaultRequestProperties().set("Content-Type", "application/x-mpegURL");
        dataSource.getDefaultRequestProperties().set("Accept", "*/*");

        player.setPlayWhenReady(playWhenReady);
        player.addListener(this);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare( new HlsMediaSource.Factory(dataSource).createMediaSource(uri, null, null), true, false);
    }
}
