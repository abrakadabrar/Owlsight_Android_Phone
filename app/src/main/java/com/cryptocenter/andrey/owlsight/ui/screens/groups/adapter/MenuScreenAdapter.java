package com.cryptocenter.andrey.owlsight.ui.screens.groups.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cryptocenter.andrey.owlsight.R;
import com.cryptocenter.andrey.owlsight.data.model.monitor.Monitor;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuScreenAdapter extends RecyclerView.Adapter<MenuScreenAdapter.ViewHolder> {

    private List<Monitor> monitors;
    private IMonitorSelected iMonitorSelected;

    public MenuScreenAdapter(List<Monitor> monitors, IMonitorSelected iMonitorSelected) {
        this.monitors = monitors;
        this.iMonitorSelected = iMonitorSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_screens, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(monitors.get(position).getViewName());
    }

    @Override
    public int getItemCount() {
        return monitors == null ? 0 : monitors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_menu_screen_name);
            itemView.setOnClickListener(v -> iMonitorSelected.monitorSelected(monitors.get(getAdapterPosition())));
        }
    }

    public interface IMonitorSelected {
        void monitorSelected(Monitor monitor);
    }
}
