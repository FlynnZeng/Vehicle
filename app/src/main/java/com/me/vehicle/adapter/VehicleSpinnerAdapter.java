package com.me.vehicle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.me.vehicle.model.Vehicle;

import java.util.List;

public class VehicleSpinnerAdapter extends ArrayAdapter<Vehicle> {

    public VehicleSpinnerAdapter(@NonNull Context context, @NonNull List<Vehicle> vehicles) {
        super(context, android.R.layout.simple_spinner_item, vehicles);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        Vehicle vehicle = getItem(position);
        assert vehicle != null;
        view.setText(vehicle.getPlateNumber());
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        Vehicle vehicle = getItem(position);
        assert vehicle != null;
        view.setText(String.format("%s - %s", vehicle.getPlateNumber(), vehicle.getBrand()));
        return view;
    }
}

