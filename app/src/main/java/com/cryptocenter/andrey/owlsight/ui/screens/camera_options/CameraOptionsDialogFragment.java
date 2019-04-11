package com.cryptocenter.andrey.owlsight.ui.screens.camera_options;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseDialogFragment;
import com.cryptocenter.andrey.owlsight.data.preferences.Preferences;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import toothpick.Toothpick;

public class CameraOptionsDialogFragment extends BaseDialogFragment implements CameraOptionsView {
    private static final String CAMERA_ID = "CAMERA_ID";

    public static CameraOptionsDialogFragment newInstance(Integer cameraId) {
        Bundle args = new Bundle();
        args.putInt(CAMERA_ID, cameraId);
        CameraOptionsDialogFragment fragment = new CameraOptionsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter
    CameraOptionsPresenter presenter;

    @BindView(R.id.switchMotionDetection)
    MaterialAnimatedSwitch switchMotionDetection;
    @BindView(R.id.clMain)
    LinearLayout clMain;
    @BindView(R.id.clDeleteConfirmation)
    LinearLayout clDeleteConfirmation;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_camera_options;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        switchMotionDetection.setOnCheckedChangeListener(checked -> {
            presenter.onMotionDetectionCheckChanged(checked);
        });
    }

    @ProvidePresenter
    public CameraOptionsPresenter providePresenter() {
        OwlsightRepository owlsightRepository = Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository.class);
        Preferences preferences = Toothpick.openScope(Scopes.APP).getInstance(Preferences.class);
        Integer cameraId = getArguments().getInt(CAMERA_ID, 0);
        return new CameraOptionsPresenter(owlsightRepository, cameraId, preferences);
    }

    @OnClick({R.id.clMotionDetection, R.id.clDeleteCamera, R.id.btnCloseOptionsDialog, R.id.btnYes, R.id.btnNo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clMotionDetection:
                presenter.onBtnMotionDetectionClicked();
                break;
            case R.id.clDeleteCamera:
                presenter.onBtnDeleteCameraClicked();
                break;
            case R.id.btnCloseOptionsDialog:
                presenter.onBtnCloseOptionsDialogClicked();
                break;
            case R.id.btnYes:
                presenter.onBtnYesClicked();
                break;
            case R.id.btnNo:
                presenter.onBtnNoClicked();
                break;
        }
    }

    @Override
    public void toggleSwitch() {
        switchMotionDetection.toggle();
    }

    @Override
    public void closeDialog() {
        dismiss();
    }

    @Override
    public void setMainLayoutVisible() {
        clMain.setVisibility(View.VISIBLE);
        clDeleteConfirmation.setVisibility(View.GONE);
    }

    @Override
    public void setConfirmationLayoutVisible() {
        clMain.setVisibility(View.GONE);
        clDeleteConfirmation.setVisibility(View.VISIBLE);
    }
}
