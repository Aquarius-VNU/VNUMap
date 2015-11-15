package com.aquarius.vnumap.adapter;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Trac Quang Thinh on 15-Nov-15.
 */
public class LocationServices implements LocationListener {
    private static LocationServices ourInstance = new LocationServices();

    public static LocationServices getInstance() {
        return ourInstance;
    }

    private LocationServices() {
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
