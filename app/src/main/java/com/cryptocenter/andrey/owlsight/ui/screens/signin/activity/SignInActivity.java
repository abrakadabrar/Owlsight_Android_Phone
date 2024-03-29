package com.cryptocenter.andrey.owlsight.ui.screens.signin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.event.ForgotPasswordEvent;
import com.cryptocenter.andrey.owlsight.ui.screens.signin.forgot_password.ForgotPasswordFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.signin.fragment.SignInFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class SignInActivity extends BaseActivity implements SignInActivityView {

    private static final String SIGN_IN_FRAGMENT_TAG = "SIGN_IN_FRAGMENT_TAG";
    private static final String FORGOT_PASSWORD_FRAGMENT_TAG = "FORGOT_PASSWORD_FRAGMENT_TAG";

    public static void start(Context context) {
        Intent starter = new Intent(context, SignInActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    public static Intent intent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @InjectPresenter
    SignInActivityPresenter signInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in_up);
        ButterKnife.bind(this);
        boolean deviceSupportsAEP = getPackageManager().hasSystemFeature
                (PackageManager.FEATURE_OPENGLES_EXTENSION_PACK);
        Log.d("AEP", "deviceSupportsAEP = " + deviceSupportsAEP);
    }

    @Override
    public void setSignInFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(SIGN_IN_FRAGMENT_TAG);

        if (fragment == null) {
            fragment = SignInFragment.newInstance();
        }

        fm.beginTransaction()
                .replace(R.id.signInContainer, fragment, SIGN_IN_FRAGMENT_TAG).commit();
    }

    @Override
    public void setForgotPasswordFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(FORGOT_PASSWORD_FRAGMENT_TAG);

        if (fragment == null) {
            fragment = ForgotPasswordFragment.newInstance();
        }

        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.signInContainer, fragment, FORGOT_PASSWORD_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onForgotPassword(ForgotPasswordEvent event) {
        signInPresenter.handleForgotPassword();
    }
}
