package com.aquarius.vnumap.ui;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.ArrayBuildings;
import com.aquarius.vnumap.adapter.LocationServices;
import com.aquarius.vnumap.controller.JSONMap;
import com.aquarius.vnumap.controller.MainController;
import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.model.Room;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
//  camera when location = null
    private  static final double LATITUDE_CAMERA = 21.03844442;
    private static final double LONGGITUDE_CAMERA = 105.78237534;
    private  static final float ZOOM_CAMERA = 17.0f;
    private static final LatLng LEFT_BOTTOM_CONNER = new LatLng(21.036921, 105.781066);
    private static final LatLng RIGHT_TOP_CONNER = new LatLng(21.040987, 105.785605);
    private static final LatLng LOCATION_VNU = new LatLng(21.036787, 105.782040);
    private GoogleMap mMap;
//  variable contain location from GPS or NETWORK
    private Location location = null;
//  marker of location
    private Marker markerLocation = null;
//  list all marker on the map
    private List<Marker> markerList = new ArrayList<>();
//  use to manage thread
    private Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        handler = new Handler();
//      update location 1s/1 turn
        updateLocation();

        FloatingActionButton fab_location = (FloatingActionButton)findViewById(R.id.fab_location);
        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                21.040987, 105.785605
//                21.036921, 105.781066

//                  delete all marker
                if (location != null) {
//                  if location in VNU
                    if(LEFT_BOTTOM_CONNER.latitude <= location.getLatitude() && location.getLatitude() <= RIGHT_TOP_CONNER.latitude
                            && LEFT_BOTTOM_CONNER.longitude <= location.getLongitude() && location.getLongitude() <= RIGHT_TOP_CONNER.longitude) {
                        if (markerList.size() > 0) {
                            for (int i = 0; i < markerList.size(); i++) {
                                markerList.get(i).setVisible(false);
                            }
                            markerList.clear();
                        }
//                      show buldings near location now
                        List<Building> buildings = ArrayBuildings.getInstance(MapsActivity.this).getBuildingsByLocation(5, location);
                        if (buildings != null) {
                            for (int i = 0; i < buildings.size(); i++) {
                                LatLng latLng = new LatLng(buildings.get(i).getLocation().getX(), buildings.get(i).getLocation().getY());
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(buildings.get(i).getName()));
                                markerList.add(marker);
                            }
                        }

//                      move camera to location
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(ZOOM_CAMERA)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setTitle("VNUMap");
                        builder.setMessage("Bạn đang ở ngoài khuôn viên ĐHQG Hà Nội. Tìm chỉ đường tới đây ?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String url = JSONMap.makeURL(location.getLatitude(), location.getLongitude(), LOCATION_VNU.latitude, LOCATION_VNU.longitude);
                                DownloadAndDrawPath downloadAndDrawPath = new DownloadAndDrawPath(url);
                                downloadAndDrawPath.execute();
                            }
                        });
                        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //move camera to location
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .zoom(ZOOM_CAMERA)
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });
                        builder.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("VNUMap");
                    builder.setMessage("Vui lòng kết nối Internet hoặc GPS");
                    builder.setPositiveButton("Đồng ý", null);
                    builder.show();
                }
            }
        });

        List<Building> list = MainController.getListBuilding(getResources().openRawResource(R.raw.map), getResources().openRawResource(R.raw.direction));
        EditText editText = (EditText)findViewById(R.id.edtSearch);
        editText.setText(list.get(0).getRooms().get(0).getInfo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//      define to get location information
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

//      add Marker
        ArrayBuildings arrayBuildings = ArrayBuildings.getInstance(this);
        List<Building> buildings = arrayBuildings.getBuildings(10);
        if(buildings != null) {
            for (int i = 0; i < buildings.size(); i++) {
                LatLng latLng = new LatLng(buildings.get(i).getLocation().getX(), buildings.get(i).getLocation().getY());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(buildings.get(i).getName()));
                markerList.add(marker);
            }
        }

//      setting camera
        LatLng posCamera = new LatLng(LATITUDE_CAMERA, LONGGITUDE_CAMERA);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(posCamera)
                .zoom(ZOOM_CAMERA)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    public void updateLocation(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    SystemClock.sleep(1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            location = LocationServices.getInstance(MapsActivity.this).getLocation();
                            if (location != null) {
                                Log.d("HANDLE", String.valueOf(location.getLatitude()));
                                if(markerLocation != null){
                                    markerLocation.setVisible(false);
                                }
                                markerLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("FUCK"));
                            } else {
                                Log.d("HANDLE", "null");
                            }
                        }
                    });
                }
            }
        });
        thread.start();
    }
//  draw path on map
    private void drawPath(String path){
        try {
            JSONObject jsonObject = new JSONObject(path);
            Log.d("MAPDRAW", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("routes");
            JSONObject routes = jsonArray.getJSONObject(0);
            JSONObject overview_polyline = routes.getJSONObject("overview_polyline");
            String polyline = overview_polyline.getString("points");
            List<LatLng> latLngs = JSONMap.decodePoly(polyline);
            mMap.addPolyline(new PolylineOptions().addAll(latLngs).width(13).color(Color.parseColor("#05b1fb")).geodesic(true));
        }catch(JSONException e){
            e.printStackTrace();

        }
    }

    private class DownloadAndDrawPath extends AsyncTask<Void, Void, String>{
        private String url = null;
        DownloadAndDrawPath(String url){
            this.url = url;
        }
        protected String doInBackground(Void... paramas){
            JSONMap jsonMap = new JSONMap();
            String result = jsonMap.getJSONFromURL(url);
            Log.d("MAPDRAW", result);
            return result;
        }
        protected  void onPostExecute(String result){
            if(result != null) {
                drawPath(result);
            }
        }
    }



}
