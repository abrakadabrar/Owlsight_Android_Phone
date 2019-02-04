package com.cryptocenter.andrey.owlsight.ui.screens.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.managers.SinglePlayerManager;
import com.google.android.exoplayer2.ui.PlayerView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SinglePlayerActivity extends BaseActivity {

    private SinglePlayerManager playerManager;

    public static Intent intent(Context context, String id) {
        final Intent intent = new Intent(context, SinglePlayerActivity.class);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        playerManager = new SinglePlayerManager(this, getIntent().getStringExtra("id"));
        (((PlayerView) findViewById(R.id.playerView))).setPlayer(playerManager.getPlayer());
    }

    @Override
    public void onPause() {
        super.onPause();
        playerManager.releasePlayer();
    }

    @OnClick(R.id.btnClose)
    void onCloseClick(View v) {
        finish();
    }
}
