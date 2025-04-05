package com.me.vehicle.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.me.vehicle.R;
import com.me.vehicle.ui.carLocale.CarLocaleFragment;

public class CarLocaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_locale);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CarLocaleFragment.newInstance())
                    .commitNow();
        }
    }
}