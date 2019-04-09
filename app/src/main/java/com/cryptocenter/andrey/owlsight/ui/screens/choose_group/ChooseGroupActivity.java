package com.cryptocenter.andrey.owlsight.ui.screens.choose_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.screens.add_camera.AddCameraActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.choose_group.adapter.ChooseGroupAdapter;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Toothpick;

public class ChooseGroupActivity extends BaseActivity implements ChooseGroupView, ChooseGroupAdapter.OnGroupNameClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, ChooseGroupActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.groupsRv)
    RecyclerView groupsRv;

    @InjectPresenter
    ChooseGroupPresenter chooseGroupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnCloseChooseGroup)
    public void onViewClicked() {
        chooseGroupPresenter.onBtnClose();
    }

    @Override
    public void onGroupNameClicked(String groupName) {
        chooseGroupPresenter.onGroupSelected(groupName);
    }

    @ProvidePresenter
    public ChooseGroupPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(ChooseGroupPresenter.class);
    }

    @Override
    public void showGroups(List<String> groupNames) {
        groupsRv.setLayoutManager(new LinearLayoutManager(this));
        groupsRv.setAdapter(new ChooseGroupAdapter(groupNames, this));
    }

    @Override
    public void addCamera(Integer cameraId) {
        finish();
        startActivity(AddCameraActivity.intent(this, cameraId));
    }

    @Override
    public void finishNoResult() {
        finish();
    }
}
