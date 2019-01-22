package com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter;

import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.ui.screens.add_group.AddGroupFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.group.GroupFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GroupsPagerAdapter extends FragmentPagerAdapter {

    private List<Group> groups;

    public GroupsPagerAdapter(FragmentManager fm, List<Group> groups) {
        super(fm);
        this.groups = groups;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return AddGroupFragment.instance();
        return GroupFragment.instance(groups.get(position).getCams());
    }

    @Override
    public int getCount() {
        return groups.size();
    }
}
