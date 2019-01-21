package com.cryptocenter.andrey.owlsight.base.moxy;

import android.os.Bundle;

import com.arellomobile.mvp.MvpDelegate;

import androidx.fragment.app.FragmentActivity;

/**
 * Date: 17.12.2015
 * Time: 14:34
 *
 * @author Yuri Shmakov
 * @author Alexander Bliniov
 * @author Konstantin Tckhovrebov
 */
@SuppressWarnings("unused")
public class MvpAndroidXActivity extends FragmentActivity {
	private MvpDelegate<? extends MvpAndroidXActivity> mMvpDelegate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getMvpDelegate().onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();

		getMvpDelegate().onAttach();
	}

	@Override
	protected void onResume() {
		super.onResume();

		getMvpDelegate().onAttach();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		getMvpDelegate().onSaveInstanceState(outState);
		getMvpDelegate().onDetach();
	}

	@Override
	protected void onStop() {
		super.onStop();

		getMvpDelegate().onDetach();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		getMvpDelegate().onDestroyView();

		if (isFinishing()) {
			getMvpDelegate().onDestroy();
		}
	}

	/**
	 * @return The {@link MvpDelegate} being used by this Activity.
	 */
	public MvpDelegate getMvpDelegate() {
		if (mMvpDelegate == null) {
			mMvpDelegate = new MvpDelegate<>(this);
		}
		return mMvpDelegate;
	}
}
