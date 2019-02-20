package com.cryptocenter.andrey.owlsight.base;

import android.os.Bundle;
import android.widget.Toast;

import com.cryptocenter.andrey.owlsight.base.moxy.MvpAndroidXActivity;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.ui.screens.add_camera.AddCameraActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.GroupsActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.menu.MenuActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.monitor.MonitorActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.player.SinglePlayerActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.register.RegisterActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.register_sms.RegisterSmsActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.signin.SignInActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.stream.StreamActivity;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.cryptocenter.andrey.owlsight.utils.Screen;
import com.kaopiz.kprogresshud.KProgressHUD;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import es.dmoral.toasty.Toasty;

public abstract class BaseActivity extends MvpAndroidXActivity implements BaseView {

    private KProgressHUD loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = Alerts.loader(this);
    }

    @Override
    public void bind() {

    }

    @Override
    public void showLoading() {
        loader.show();
    }

    @Override
    public void hideLoading() {
        loader.dismiss();
    }

    @Override
    public void showFailed() {
        Toasty.error(this, "Что - то пошло не так...", Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showError(Throwable error) {
        Toasty.error(this, "Ошибка сети, проверьте интернет соединение ", Toast.LENGTH_SHORT, true).show();
    }

    public void onBackClicked() {
        finish();
    }

    public void addScreen(Screen screen, Object data) {
        switch (screen) {
            case REGISTER:
                startActivity(RegisterActivity.intent(this));
                break;
            case STREAM:
                startActivity(StreamActivity.intent(this));
                break;
            case MENU:
                startActivity(MenuActivity.intent(this));
                break;
            case MONITOR:
                startActivity(MonitorActivity.intent(this, (Monitor) data));
                break;
            case SINGLE_PLAYER:
                startActivity(SinglePlayerActivity.intent(this, (String) data));
                break;
            case ADD_CAMERA:
                startActivity(AddCameraActivity.intent(this, Integer.parseInt((String) data)));
                break;
        }
    }

    public void addScreenForResult(Screen screen, int requestCode, Object... data) {
        switch (screen) {
            case REGISTER_SMS:
                startActivityIfNeeded(RegisterSmsActivity.intent(this, (RegisterData) data[0]), requestCode);
        }
    }

    public void showScreen(Screen screen) {
        finish();
        switch (screen) {
            case SIGN_IN:
                startActivity(SignInActivity.intent(this));
                break;
            case GROUPS:
                startActivity(GroupsActivity.intent(this));
                break;
        }
    }

    public void closeScreen(@Nullable String message) {
        if (message != null) showMessage(message);
        if (loader != null) loader.dismiss();
        finish();
    }

    public void closeScreenWithResult(int resultCode, String message) {
        if (message != null) showMessage(message);
        if (loader != null) loader.dismiss();
        setResult(resultCode);
        finish();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showAlertMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", (dialogInterface, i) -> {
        }).show();
    }
}
