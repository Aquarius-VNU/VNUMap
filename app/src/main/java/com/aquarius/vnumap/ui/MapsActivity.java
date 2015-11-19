package com.aquarius.vnumap.ui;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.ArrayBuildings;
import com.aquarius.vnumap.adapter.LocationServices;
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

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private  static final double LATITUDE_CAMERA = 21.03844442;
    private static final double LONGGITUDE_CAMERA = 105.78237534;
    private  static final float ZOOM_CAMERA = 17.0f;
    private GoogleMap mMap;
    private Location location = null;
    private List<Marker> markerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab_location = (FloatingActionButton)findViewById(R.id.fab_location);
        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = LocationServices.getInstance(MapsActivity.this.getBaseContext()).getLocation();
                if(location != null) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("FUCK"));
//                  delete all marker
                    if(markerList.size() > 0){
                        for(int i = 0 ; i < markerList.size() ; i++){
                            markerList.get(i).setVisible(false);
                        }
                        markerList.clear();
                    }
//                  show buldings near location now
                    List<Building> buildings = ArrayBuildings.getInstance(MapsActivity.this).getBuildingsByLocation(5, location);
                    if(buildings != null) {
                        for (int i = 0; i < buildings.size(); i++) {
                            LatLng latLng = new LatLng(buildings.get(i).getLocation().getX(), buildings.get(i).getLocation().getY());
                            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(buildings.get(i).getName()));
                            markerList.add(marker);
                        }
                    }

                }else{
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

}
