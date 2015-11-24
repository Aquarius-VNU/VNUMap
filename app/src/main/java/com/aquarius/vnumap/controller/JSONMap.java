package com.aquarius.vnumap.controller;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Trac Quang Thinh on 21-Nov-15.
 */
public class JSONMap {
    private static final String key = "AIzaSyBVtAuyOypVf1uhy4U-DO5UYnBZKDYh2IE";

    public JSONMap() {
    }

    //  provide url to get json
    public static String makeURL(double originLat, double originLong, double desLat, double desLong){
        StringBuilder url = new StringBuilder();
        url.append("https://maps.googleapis.com/maps/api/directions/json");
//      start
        url.append("?origin=");
        url.append(Double.toString(originLat));
        url.append(",");
        url.append(Double.toString(originLong));
//      stop
        url.append("&destination=");
        url.append(Double.toString(desLat));
        url.append(",");
        url.append(Double.toString(desLong));
        url.append("&sensor=false&mode=driving&alternatives=true");
        url.append("&key=");
        url.append(key);
        return url.toString();
    }

    public String getJSONFromURL(String urlString){
//      read file downloaded and convert to string
        String result = null;
        try{
            result = new Scanner(new URL(urlString).openConnection().getInputStream()).useDelimiter("/z").next();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
//  copyright : zeeshan0026 - stackoverflow
    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
