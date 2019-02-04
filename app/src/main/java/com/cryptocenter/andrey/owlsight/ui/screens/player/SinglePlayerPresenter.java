package com.cryptocenter.andrey.owlsight.ui.screens.player;

import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BasePresenter;

import butterknife.OnClick;

@InjectViewState
public class SinglePlayerPresenter extends BasePresenter<SinglePlayerView> {


    @OnClick(R.id.btnClose)
    void onCloseClick(View v) {

    }


}
