package com.cryptocenter.andrey.owlsight.ui.screens.add_camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.repository.owlsight.OwlsightRepository;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Toothpick;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class AddCameraActivity extends BaseActivity implements AddCameraView {

    private static final String ID = "AddCameraActivity.ID";

    @InjectPresenter
    AddCameraPresenter presenter;
   

    public static Intent intent(Context context, int cameraId) {
        final Intent intent = new Intent(context, AddCameraActivity.class);
        intent.putExtra(ID, cameraId);
        return intent;
    }

    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_camera);
        ButterKnife.bind(this);
    }

    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    AddCameraPresenter providePresenter() {
        return new AddCameraPresenter(
                Toothpick.openScope(Scopes.APP).getInstance(OwlsightRepository.class),
                getIntent().getIntExtra(ID, -1));
    }


    @Override
    public void showTestResult(String message) {
        textViewdata.setText(message);
    }
}


