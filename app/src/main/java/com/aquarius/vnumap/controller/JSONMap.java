package com.aquarius.vnumap.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Trac Quang Thinh on 21-Nov-15.
 */
public class JSONMap {
    private static final String key = "AIzaSyD5h-bZGHNZGkvth-BnS0PP7SdyubThj8s";

    public JSONMap() {
    }

    //  provide url to get json
    public String makeURL(double originLat, double originLong, double desLat, double desLong){
        StringBuilder url = new StringBuilder();
        url.append("http://maps.googleapis.com/maps/api/directions/json");
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
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
//      connect and get json file from google
        try{
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            inputStream = httpURLConnection.getInputStream();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }
//      read file downloaded and convert to string
        StringBuilder stringBuilder = new StringBuilder();
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);

            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
        }catch(UnsupportedEncodingException e){

        }catch (IOException e){

        }
        return stringBuilder.toString();
    }

}
