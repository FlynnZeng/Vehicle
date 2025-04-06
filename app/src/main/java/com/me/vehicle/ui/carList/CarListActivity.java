package com.me.vehicle.ui.carList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.me.vehicle.R;

public class CarListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CarListFragment.newInstance())
                    .commitNow();
        }
    }
}