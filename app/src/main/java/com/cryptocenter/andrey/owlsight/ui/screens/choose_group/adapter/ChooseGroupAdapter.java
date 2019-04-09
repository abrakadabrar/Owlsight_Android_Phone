package com.cryptocenter.andrey.owlsight.ui.screens.choose_group.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cryptocenter.andrey.owlsight.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseGroupAdapter extends RecyclerView.Adapter<ChooseGroupAdapter.ChooseGroupViewHolder> {

    private final List<String> groupNames;
    private final OnGroupNameClickListener listener;

    public ChooseGroupAdapter(List<String> groupNames, OnGroupNameClickListener listener) {
        this.groupNames = groupNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChooseGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new ChooseGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseGroupViewHolder holder, int position) {
        String groupName = groupNames.get(position);
        holder.tvGroupName.setText(groupName);
        holder.btnGroupName.setOnClickListener(v -> listener.onGroupNameClicked(groupName));
    }

    @Override
    public int getItemCount() {
        return groupNames.size();
    }

    public static class ChooseGroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnGroupName)
        LinearLayout btnGroupName;
        @BindView(R.id.tvGroupName)
        AppCompatTextView tvGroupName;

        public ChooseGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnGroupNameClickListener{
        void onGroupNameClicked(String groupName);
    }
}
