package com.cryptocenter.andrey.owlsight.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cryptocenter.andrey.owlsight.base.moxy.MvpAndroidXFragment;
import com.cryptocenter.andrey.owlsight.data.model.data.FromDateData;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.GroupsActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.player.SinglePlayerActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.video.FromDateActivity;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.cryptocenter.andrey.owlsight.utils.Screen;
import com.kaopiz.kprogresshud.KProgressHUD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import es.dmoral.toasty.Toasty;

public abstract class BaseFragment extends MvpAndroidXFragment implements BaseView {

    protected int layoutResId;
    private KProgressHUD loader;

    public int getLayoutResId() {
        return layoutResId;
    }

    @Override
    public void bind() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void showLoading() {
        loader = Alerts.loader(getActivity());
        loader.show();
    }

    @Override
    public void hideLoading() {
        if (loader != null) {
            loader.dismiss();
            loader = null;
        }
    }

    @Override
    public void showFailed() {
        Toasty.error(getActivity(), "Что - то пошло не так...", Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showError(Throwable error) {
        Toasty.error(getActivity(), "Ошибка сети, проверьте интернет соединение ", Toast.LENGTH_SHORT, true).show();
    }

    public void addScreen(Screen screen, Object data) {
        switch (screen) {
            case SINGLE_PLAYER:
                startActivity(SinglePlayerActivity.intent(getActivity(), (String) data));
                break;
            case VIDEO_FROM_DATE:
               startActivity(FromDateActivity.intent(getActivity(), (FromDateData) data));
                break;
        }
    }

    @Override
    public void addScreenForResult(Screen screen, int requestCode, Object... data) {

    }

    public void showScreen(Screen screen) {
        if (loader.isShowing()) hideLoading();
        getActivity().finish();
        switch (screen) {
            case GROUPS:
                getActivity().startActivity(GroupsActivity.intent(getActivity()));
                break;
        }
    }

    public void closeScreen(@Nullable String message) {
        if (message != null) showMessage(message);
        getActivity().finish();
    }

    @Override
    public void closeScreenWithResult(int resultCode, String message) {

    }

    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlertMessage(String title, String message) {
        new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message).setPositiveButton("OK", (dialogInterface, i) -> {
        }).show();
    }

    @Override
    public void onBackClicked() {

    }
}
