package com.cryptocenter.andrey.owlsight.ui.screens.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.base.BaseActivity;
import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.di.Scopes;
import com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter.GroupsPagerAdapter;
import com.cryptocenter.andrey.owlsight.utils.Alerts;

import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Toothpick;

public class GroupsActivity extends BaseActivity implements GroupsView {

    @InjectPresenter
    GroupsPresenter presenter;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public static Intent intent(Context context) {
        return new Intent(context, GroupsActivity.class);
    }


    // =============================================================================================
    // Android
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);
        setupUI();
    }


    // =============================================================================================
    // View
    // =============================================================================================

    @Override
    public void setGroups(List<Group> groups) {
        presenter.handlePageSelected(1);
        pager.setAdapter(new GroupsPagerAdapter(getSupportFragmentManager(), groups));
        pager.setCurrentItem(groups.size() > 0 ? 1 : 0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                presenter.handlePageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void setGroupTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showAlertGroupEdit(int position, String oldTitle) {
        Alerts.showGroupEditDialog(this, oldTitle, newTitle -> presenter.handleOkEditGroupClick(position, newTitle));
    }

    @Override
    public void restart() {

    }

    // =============================================================================================
    // Private
    // =============================================================================================

    private void setupUI() {
        findViewById(R.id.btnMenu).setOnClickListener(v -> presenter.handleMenuClick());
        tvTitle.setOnClickListener(v -> presenter.handleTitleClick(pager.getCurrentItem()));
    }


    // =============================================================================================
    // Moxy
    // =============================================================================================

    @ProvidePresenter
    GroupsPresenter providePresenter() {
        return Toothpick.openScope(Scopes.APP).getInstance(GroupsPresenter.class);
    }
}
