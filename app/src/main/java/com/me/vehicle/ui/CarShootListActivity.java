package com.me.vehicle.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.me.vehicle.R;
import com.me.vehicle.ui.carShootList.CarShootListFragment;

public class CarShootListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_shoot_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CarShootListFragment.newInstance())
                    .commitNow();
        }
    }
}