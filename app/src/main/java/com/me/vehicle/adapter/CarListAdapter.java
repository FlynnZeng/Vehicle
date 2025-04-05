package com.me.vehicle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.vehicle.R;
import com.me.vehicle.callback.CarListCallback;
import com.me.vehicle.model.Vehicle;
import com.me.vehicle.utils.Https;
import com.me.vehicle.utils.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    private List<Vehicle> carList;
    private Context context;
    private CarListCallback callback;

    public CarListAdapter(Context context, List<Vehicle> carList, CarListCallback callback) {
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

        Log.i("link", Https.BASEURL+car.getCover());
        ImageLoader.loadImage(holder.carImage, Https.BASEURL+car.getCover());
        holder.plateText.setText(car.getPlateNumber());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


        holder.infoText.setText(String.format("上次保养: %s | 行驶里程: %d KM", sdf.format(car.getLastMaintenance()), car.getMileage()));

        holder.itemView.setOnClickListener(v-> this.callback.onItemClick(car));
    }

    @Override
    public int getItemCount() {
        return carList != null ? carList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView plateText;
        TextView infoText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.car_cover);
            plateText = itemView.findViewById(R.id.car_num);
            infoText = itemView.findViewById(R.id.car_basic);
        }
    }
}

