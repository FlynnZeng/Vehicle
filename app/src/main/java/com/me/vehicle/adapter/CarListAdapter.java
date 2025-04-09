package com.me.vehicle.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.vehicle.R;
import com.me.vehicle.callback.ItemCallback;
import com.me.vehicle.model.CostStatistics;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.model.VehicleUse;
import com.me.vehicle.ui.carCost.CarCostActivity;
import com.me.vehicle.ui.carLocale.CarLocaleActivity;
import com.me.vehicle.utils.Https;
import com.me.vehicle.utils.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    private List<Vehicle> carList;
    private Context context;
    private ItemCallback<Vehicle> callback;

    public CarListAdapter(Context context, List<Vehicle> carList, ItemCallback<Vehicle> callback) {
        this.context = context;
        this.carList = carList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle car = carList.get(position);

        ImageLoader.loadImage(holder.carImage, Https.BASEURL+car.getCover());
        holder.plateText.setText(car.getPlateNumber());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


        holder.infoText.setText(String.format("上次保养: %s | 行驶里程: %d KM", sdf.format(car.getLastMaintenance()), car.getMileage()));

        holder.itemView.setOnClickListener(v-> this.callback.onClick(car));

        holder.curLocal.setOnClickListener(v->{
            Intent intent = new Intent(context, CarLocaleActivity.class);
            intent.putExtra("item", car);
            context.startActivity(intent);
        });

        holder.carReport.setOnClickListener(v->{
            Intent intent = new Intent(context, CarCostActivity.class);
            intent.putExtra("car", car);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carList != null ? carList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView plateText, curLocal, carReport, infoText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.car_cover);
            plateText = itemView.findViewById(R.id.car_num);
            infoText = itemView.findViewById(R.id.car_basic);
            carReport = itemView.findViewById(R.id.car_report);
            curLocal = itemView.findViewById(R.id.cur_local);
        }
    }
}

