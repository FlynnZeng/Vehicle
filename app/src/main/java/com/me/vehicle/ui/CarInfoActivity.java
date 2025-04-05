package com.me.vehicle.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.me.vehicle.R;
import com.me.vehicle.ui.carInfo.CarInfoFragment;

public class CarInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CarInfoFragment.newInstance())
                    .commitNow();
        }
    }
}