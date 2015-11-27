package com.aquarius.vnumap.ui;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.TimeInterpolator;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Outline;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.ArrayBuildings;
import com.aquarius.vnumap.adapter.LocationServices;
import com.aquarius.vnumap.adapter.MapMarker;
import com.aquarius.vnumap.controller.JSONMap;
import com.aquarius.vnumap.controller.MainController;
import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.model.Room;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;
import java.util.zip.Inflater;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
//  camera when location = null
    private  static final double LATITUDE_CAMERA = 21.03844442;
    private static final double LONGGITUDE_CAMERA = 105.78237534;
    private  static final float ZOOM_CAMERA = 17.0f;
    private static final LatLng LEFT_BOTTOM_CONNER = new LatLng(21.036921, 105.781066);
    private static final LatLng RIGHT_TOP_CONNER = new LatLng(21.040987, 105.785605);
    private static final LatLng LOCATION_VNU = new LatLng(21.036787, 105.782040);
    private static final String[] UNIVERSITY_STRINGS={"Đại học Quốc Gia Hà Nội", "Đại học Công Nghệ - ĐHQG Hà Nội",
            "Đại học Ngoại Ngữ - ĐHQG Hà Nội", "Khoa Luật - ĐHQG Hà Nội", "Khoa Y Dược - ĐHQG Hà Nội",
            "Khoa Quốc Tế - ĐHQG Hà Nội", "Đại học Giáo Dục - ĐHQG Hà Nội", "Đại học Kinh Tế - ĐHQG Hà Nội", "Đại học Sư Phạm Hà Nội"};
    private GoogleMap mMap;
//  variable contain location from GPS or NETWORK
    private Location location = null;
//  marker of location
    private Marker markerLocation = null;
//  list all marker on the map
    private List<MapMarker> markerList = new ArrayList<>();
//  use to manage thread
    private Handler handler = null;

    private ViewGroup mContainer;

    SlidingUpPanelLayout slidingUpPanelLayout = null;

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
                    if (LEFT_BOTTOM_CONNER.latitude <= location.getLatitude() && location.getLatitude() <= RIGHT_TOP_CONNER.latitude
                            && LEFT_BOTTOM_CONNER.longitude <= location.getLongitude() && location.getLongitude() <= RIGHT_TOP_CONNER.longitude) {
                        if (markerList.size() > 0) {
                            for (int i = 0; i < markerList.size(); i++) {
                                markerList.get(i).getMarker().setVisible(false);
                            }
                            markerList.clear();
                        }
//                      show buldings near location now
                        List<Building> buildings = ArrayBuildings.getInstance(MapsActivity.this).getBuildingsByLocation(5, location);
                        addMarker(buildings);

//                      move camera to location
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(ZOOM_CAMERA)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    } else {
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
        fab_location.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab_building);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, BuildingActivity.class);
                startActivity(intent);
                finish();
            }
        });



        slidingUpPanelLayout  = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);

        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                TextView textView = (TextView)findViewById(R.id.sliding_panel_txtViewName);
                RelativeLayout header = (RelativeLayout)findViewById(R.id.sliding_panel_header);
                TextView name = (TextView)findViewById(R.id.sliding_panel_txtViewName);
                TextView university = (TextView)findViewById(R.id.sliding_panel_txtViewUniversity);
                if(v > 0) {
                    header.setBackgroundColor(getResources().getColor(R.color.blue));
                    name.setTextColor(getResources().getColor(R.color.white));
                    university.setTextColor(getResources().getColor(R.color.white));
                }else{
                    header.setBackgroundColor(Color.WHITE);
                    name.setTextColor(Color.BLACK);
                    university.setTextColor(getResources().getColor(R.color.button_material_dark));
                }
            }

            @Override
            public void onPanelCollapsed(View view) {

            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        mContainer = (ViewGroup)findViewById(R.id.map_container);
        LayoutTransition transition =new LayoutTransition();

        transition.setAnimator(LayoutTransition.APPEARING,null);
        mContainer.setLayoutTransition(transition);
        EditText edtSearch = (EditText)findViewById(R.id.edtSearch);
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(MapsActivity.this).inflate(R.layout.list_item_search, mContainer, false);
                mContainer.addView(viewGroup);
            }
        });
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                slidingUpPanelLayout.setPanelHeight(0);
            }
        });

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

//      add Marker
        ArrayBuildings arrayBuildings = ArrayBuildings.getInstance(this);
        List<Building> buildings = arrayBuildings.getBuildings(10);
        addMarker(buildings);

//      setting camera
        LatLng posCamera = new LatLng(LATITUDE_CAMERA, LONGGITUDE_CAMERA);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(posCamera)
                .zoom(ZOOM_CAMERA)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(int i = 0 ; i < markerList.size() ; i++){
                    if(marker.equals(markerList.get(i).getMarker())){
                        Building building = ArrayBuildings.getInstance(MapsActivity.this).getBuildingById(markerList.get(i).getId());
                        if(building != null){
                            TextView name = (TextView)findViewById(R.id.sliding_panel_txtViewName);
                            name.setText(String.valueOf(building.getName()));
                            TextView university = (TextView)findViewById(R.id.sliding_panel_txtViewUniversity);
                            if(building.getUniversity() > 0){
                                if(building.getUniversity() <= UNIVERSITY_STRINGS.length){
                                    university.setText(String.valueOf(UNIVERSITY_STRINGS[building.getUniversity() - 1]));
                                }else{
                                    university.setText(String.valueOf(UNIVERSITY_STRINGS[0]));
                                }
                            }else{
                                university.setText(String.valueOf(UNIVERSITY_STRINGS[0]));
                            }
                        }
                        slidingUpPanelLayout.setPanelHeight(140);
                        break;
                    }
                }
                return true;

            }
        });
    }

//  add Marker and set method on click to show sliding panel
    public void addMarker(List<Building> buildings){
        if(buildings != null) {
            for (int i = 0; i < buildings.size(); i++) {
                LatLng latLng = new LatLng(buildings.get(i).getLocation().getX(), buildings.get(i).getLocation().getY());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(buildings.get(i).getName()));
                markerList.add(new MapMarker(buildings.get(i).getId(), marker));
            }
        }
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
                                markerLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).title("FUCK"));
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
