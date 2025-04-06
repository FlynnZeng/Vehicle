package com.me.vehicle.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.vehicle.R;
import com.me.vehicle.model.VehicleUse;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CarUseAdapter extends RecyclerView.Adapter<CarUseAdapter.ViewHolder> {

    private List<VehicleUse> dataList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public CarUseAdapter(List<VehicleUse> dataList) {
        this.dataList = dataList;
    }

    public void updateData(List<VehicleUse> newList) {
        this.dataList.clear();
        this.dataList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarUseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car_use, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarUseAdapter.ViewHolder holder, int position) {
        VehicleUse item = dataList.get(position);

        holder.textDate.setText(String.format("日期: %s", dateFormat.format(item.getApplyDate())));
        holder.textTime.setText(String.format("时间: %s - %s", item.getStartTime(), item.getEndTime()));
        holder.textUsername.setText(String.format("用车人: %s", item.getUsername()));
        holder.textReason.setText(String.format("用车事由: %s", item.getReason()));
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textTime, textUsername, textReason;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.use_date);
            textTime = itemView.findViewById(R.id.use_time);
            textUsername = itemView.findViewById(R.id.use_user);
            textReason = itemView.findViewById(R.id.use_desc);
        }
    }
}

