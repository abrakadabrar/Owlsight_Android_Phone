package com.cryptocenter.andrey.owlsight.ui.screens.signin.forgot_password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordFragment extends BaseFragment implements ForgotPasswordView {

    public static ForgotPasswordFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter
    ForgotPasswordPresenter presenter;
    @BindView(R.id.etEmail)
    EditText etEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick({R.id.btnSendNewPassword, R.id.btnAuthorization})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSendNewPassword:
                presenter.handleButtonSendNewPasswordClicked();
                break;
            case R.id.btnAuthorization:
                presenter.handleButtonAuthorizationClicked();
                break;
        }
    }

    @Override
    public void popBackStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
