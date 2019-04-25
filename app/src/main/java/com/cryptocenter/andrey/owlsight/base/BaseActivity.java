package com.cryptocenter.andrey.owlsight.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cryptocenter.andrey.owlsight.App;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.moxy.MvpAndroidXActivity;
import com.cryptocenter.andrey.owlsight.data.model.data.RegisterData;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;
import com.cryptocenter.andrey.owlsight.ui.screens.add_camera.AddCameraActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.change_password.ChangePasswordFragmentDialog;
import com.cryptocenter.andrey.owlsight.ui.screens.change_phone.ChangePhoneDialogFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.choose_group.ChooseGroupActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.GroupsActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.leave_profile.LeaveProfileDialogFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.menu.MenuActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.monitor.MonitorActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.player.SinglePlayerActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.profile.ProfileActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.register.RegisterActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.register_sms.RegisterSmsActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.signin.activity.SignInActivity;
import com.cryptocenter.andrey.owlsight.ui.screens.stream.StreamActivity;
import com.cryptocenter.andrey.owlsight.utils.Alerts;
import com.cryptocenter.andrey.owlsight.utils.Screen;
import com.kaopiz.kprogresshud.KProgressHUD;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.drakeet.support.toast.ToastCompat;

public abstract class BaseActivity extends MvpAndroidXActivity implements BaseView {

    private KProgressHUD loader;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = Alerts.loader(this);
        compositeDisposable = new CompositeDisposable();
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
        Toasty.error(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showError(Throwable error) {
        Toasty.error(this, getString(R.string.network_error), Toast.LENGTH_SHORT, true).show();
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
                StreamActivity.start(this);
                break;
            case GROUPS:
                break;
            case MENU:
                startActivity(MenuActivity.intent(this));
                break;
            case MONITOR:
                startActivity(MonitorActivity.intent(this, (Monitor) data));
                break;
            case REGISTER_SMS:
                break;
            case SIGN_IN:
                break;
            case SINGLE_PLAYER:
                startActivity(SinglePlayerActivity.intent(this, (String) data));
                break;
            case ADD_CAMERA:
                startActivity(AddCameraActivity.intent(this, Integer.parseInt((String) data)));
                break;
            case CHOOSE_GROUP:
                ChooseGroupActivity.start(this);
                break;
            case PROFILE:
                ProfileActivity.start(this);
                break;
            case VIDEO_FROM_DATE:
                break;
            case CHANGE_PHONE:
                ChangePhoneDialogFragment.newInstance().show(getSupportFragmentManager(), "change_phone");
                break;
            case LEAVE_ACCOUNT:
                LeaveProfileDialogFragment.newInstance().show(getSupportFragmentManager(), "leave_profile");
                break;
            case CHANGE_PASSWORD:
                ChangePasswordFragmentDialog.newInstance().show(getSupportFragmentManager(), "change_password");
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
                finish();
                startActivity(GroupsActivity.intent(this));
                break;
        }
    }

    public void closeScreen(@Nullable String message) {
        if (message != null) showMessage(message);
        if (loader != null) loader.dismiss();
        finish();
    }

    protected void disposeOnDestroy(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    public void closeScreenWithResult(int resultCode, String message) {
        if (message != null) showMessage(message);
        if (loader != null) loader.dismiss();
        setResult(resultCode);
        finish();
    }

    public void showMessage(String message) {
        ToastCompat.makeText(App.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public void showAlertMessage(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("OK", (dialogInterface, i) -> {
        }).show();
    }
}
