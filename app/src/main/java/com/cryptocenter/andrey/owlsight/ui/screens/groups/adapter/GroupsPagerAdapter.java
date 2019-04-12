package com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter;

import com.cryptocenter.andrey.owlsight.data.model.Group;
import com.cryptocenter.andrey.owlsight.ui.screens.add_group.AddGroupFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.group.GroupFragment;
import com.cryptocenter.andrey.owlsight.ui.screens.new_add_camera_fragment.NewAddCameraFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class GroupsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Group> groups;
    private GroupFragment.IGroupsRefresh iGroupsRefresh;

    public GroupsPagerAdapter(FragmentManager fm, GroupFragment.IGroupsRefresh iGroupsRefresh, List<Group> groups) {
        super(fm);
        this.iGroupsRefresh = iGroupsRefresh;
        this.groups = groups;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return NewAddCameraFragment.Companion.newInstance();
        } else if (position == 0) {
            return AddGroupFragment.instance();
        } else {
            return GroupFragment.instance(groups.get(position).getCams(), iGroupsRefresh, groups.get(position).getGroupName(), groups.get(position).getId());
        }
    }


    @Override
    public int getCount() {
        return groups.size();
    }
}
