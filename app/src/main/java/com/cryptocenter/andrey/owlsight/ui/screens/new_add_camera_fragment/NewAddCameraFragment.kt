package com.cryptocenter.andrey.owlsight.ui.screens.new_add_camera_fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.cryptocenter.andrey.owlsight.R
import com.cryptocenter.andrey.owlsight.base.BaseFragment
import com.cryptocenter.andrey.owlsight.di.Scopes
import com.cryptocenter.andrey.owlsight.ui.screens.choose_group.adapter.ChooseGroupAdapter
import kotlinx.android.synthetic.main.fragment_new_add_camera.*
import toothpick.Toothpick

class NewAddCameraFragment : BaseFragment(), NewAddCameraView{

    @InjectPresenter
    lateinit var presenter: NewAddCameraPresenter

    lateinit var unbinder: Unbinder

    override fun getLayoutResId(): Int {
        return R.layout.fragment_new_add_camera
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)
    }

    override fun onDestroyView() {
        unbinder.unbind()
        super.onDestroyView()
    }

    @OnClick(R.id.btnEnterCameraMode, R.id.btnAddIpCamera)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btnEnterCameraMode -> presenter.onBtnEnterCameraModeClicked()
            R.id.btnAddIpCamera -> presenter.onBtnAddIpCameraClicked()
        }
    }

    companion object {
        fun newInstance(): NewAddCameraFragment {
            return NewAddCameraFragment()
        }
    }
}
