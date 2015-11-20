package com.aquarius.vnumap.adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import java.security.acl.LastOwnerException;

/**
 * Created by Trac Quang Thinh on 15-Nov-15.
 */
public class LocationServices implements LocationListener {
    private final static long MIN_TIME = 0;
    private final static long MIN_DISTANCE = 0;

    private static LocationServices ourInstance = null;
    private static LocationManager locationManager;
    private static Location location;
    private static boolean isNetworkOrGPS = false;

    public static LocationServices getInstance(Context context) {
        ourInstance = new LocationServices(context);
        return ourInstance;
    }

    private LocationServices(Context context) {
        init(context);
    }

    public Location getLocation(){
        return location;
    }

    @TargetApi(23)
    private void init(Context context){
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }return;
        }

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isNetwork && !isGPS){
            isNetworkOrGPS = false;
        }else{
            isNetworkOrGPS = true;
            if(isNetwork){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                if(locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if(isGPS){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                if(locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

        }
    }

    public void onProviderDisabled(String provider){

    }

    public void onStatusChanged(String provider, int status, Bundle extras){

    }

    public void onProviderEnabled(String provider){

    }

    public void onLocationChanged(Location location){

    }
}
