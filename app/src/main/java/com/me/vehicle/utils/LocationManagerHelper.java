package com.me.vehicle.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

public class LocationManagerHelper {

    private static LocationManagerHelper instance;
    private LocationManager locationManager;
    private String locationProvider;

    public interface OnAddressResult {
        void onResult(List<Address> addressList);
    }

    public static LocationManagerHelper getInstance() {
        if (instance == null) {
            instance = new LocationManagerHelper();
        }
        return instance;
    }

    public void build(Context context, OnAddressResult callback) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return;

        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Log.d("LocationHelper", "没有可用的位置提供器");
            return;
        }

        if (checkSelfPermission(context)) return;

        Location lastLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastLocation != null) {
            getAddress(context, lastLocation, callback);
        } else {
            locationManager.requestLocationUpdates(locationProvider, 0L, 0F,
                    getLocationListener(context, callback));
        }
    }

    private LocationListener getLocationListener(Context context, OnAddressResult callback) {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getAddress(context, location, callback);
                if (locationManager != null) {
                    locationManager.removeUpdates(this);
                    locationManager = null;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                if (locationManager != null) {
                    locationManager.removeUpdates(this);
                    locationManager = null;
                }
            }
        };
    }

    private boolean checkSelfPermission(Context context) {
        return Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private void getAddress(Context context, Location location, OnAddressResult callback) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                result = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.onResult(result);
    }
}

