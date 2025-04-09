package com.me.vehicle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.vehicle.R;
import com.me.vehicle.model.CostStatistics;

import java.util.List;
import java.util.Locale;

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.CostViewHolder> {

    private final Context context;
    private final List<CostStatistics> costList;

    public CostAdapter(Context context, List<CostStatistics> costList) {
        this.context = context;
        this.costList = costList;
    }

    @NonNull
    @Override
    public CostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cost, parent, false);
        return new CostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CostViewHolder holder, int position) {
        CostStatistics item = costList.get(position);
        holder.textDate.setText(item.getCostDate());
        holder.textType.setText(item.getCostType());

        // 金额保留 2 位小数显示
        String amountStr = String.format(Locale.CHINA, "¥%.2f", item.getAmount().doubleValue());
        holder.textAmount.setText(amountStr);
    }

    @Override
    public int getItemCount() {
        return costList.size();
    }

    public static class CostViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textType, textAmount;

        public CostViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.text_date);
            textType = itemView.findViewById(R.id.text_type);
            textAmount = itemView.findViewById(R.id.text_amount);
        }
    }
}

