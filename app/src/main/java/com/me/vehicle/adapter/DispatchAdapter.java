package com.me.vehicle.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.vehicle.R;
import com.me.vehicle.model.Dispatch;
import com.me.vehicle.model.VehicleUse;

import java.util.ArrayList;
import java.util.List;

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.DispatchViewHolder> {

    private List<VehicleUse> dispatchList = new ArrayList<>();

    public void setData(List<VehicleUse> data) {
        dispatchList.clear();
        if (data != null) {
            dispatchList.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DispatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new DispatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DispatchViewHolder holder, int position) {
        VehicleUse item = dispatchList.get(position);

        // 假设 Dispatch 类中有相应字段
        holder.timeText.setText(String.format("%s %s - %s", item.getApplyDate(), item.getStartTime(), item.getEndTime()));
        holder.statusText.setText(getState(item.getStatus()));
        holder.plateText.setText(String.format("车牌号： %s", item.getPlateNumber()));
        holder.userText.setText(String.format("用车人： %s", item.getUsername()));
        holder.reasonText.setText(String.format("用车事由： %s", item.getReason()));
        holder.routeText.setText(String.format("起点终点： %s → %s", item.getStartLocation(), item.getEndLocation()));
    }

    @Override
    public int getItemCount() {
        return dispatchList.size();
    }

    private String getState(String state) {
        switch (state){
            case "pending":
                return "待审核";
            case "approved":
                return "已批准";
            default:
                return "使用中";
        }
    }

    static class DispatchViewHolder extends RecyclerView.ViewHolder {
        TextView timeText, statusText, plateText, userText, reasonText, routeText;

        public DispatchViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.time_text);
            statusText = itemView.findViewById(R.id.status_text);
            plateText = itemView.findViewById(R.id.plate_text);
            userText = itemView.findViewById(R.id.user_text);
            reasonText = itemView.findViewById(R.id.reason_text);
            routeText = itemView.findViewById(R.id.route_text);
        }
    }
}

