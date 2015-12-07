package com.aquarius.vnumap.ui;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.TimeInterpolator;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.ArrayBuildings;
import com.aquarius.vnumap.adapter.ListMenuAdapter;
import com.aquarius.vnumap.adapter.ListSearchAdapter;
import com.aquarius.vnumap.adapter.LocationServices;
import com.aquarius.vnumap.adapter.MapMarker;
import com.aquarius.vnumap.adapter.SearchFilter;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
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
    private static final double LATITUDE_CAMERA = 21.03844442;
    private static final double LONGGITUDE_CAMERA = 105.78237534;
    private static final float ZOOM_CAMERA = 17.0f;
    private static final LatLng LEFT_BOTTOM_CONNER = new LatLng(21.036921, 105.781066);
    private static final LatLng RIGHT_TOP_CONNER = new LatLng(21.040987, 105.785605);
    private static final LatLng LOCATION_VNU = new LatLng(21.036787, 105.782040);

    private GoogleMap mMap;
    //  variable contain location from GPS or NETWORK
    private Location location = null;
    //  marker of location
    private Marker markerLocation = null;
    // circle of location
    private Circle circleLocation = null;
    //  list all marker on the map
    private List<MapMarker> markerList = new ArrayList<>();
    //  use to manage thread
    private Handler handler = null;

    private boolean isSearch = false;

    SlidingUpPanelLayout slidingUpPanelLayout = null;

    //  handle ponyline to show path
    private Polyline path = null;
    //  intent to get data
    private Intent intent;
    //  get setting data
    private SharedPreferences setting = null;
    private int numberofMarker;
//  check when id sent from building detail to direction
    private boolean isDrawBuildingDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        isDrawBuildingDetail = true;
        intent = getIntent();

        setting = getSharedPreferences(SettingActivity.KEY_PREFERENCES, 0);
        numberofMarker = setting.getInt(SettingActivity.KEY_NUMBER_MARKER, 10);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        handler = new Handler();
//      update location 1s/1 turn
        updateLocation();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    List<Building> buildings = MainController.getListBuilding(getResources().openRawResource(R.raw.map)
                            , getResources().openRawResource(R.raw.direction));
                    Bundle extras = getIntent().getExtras();

                    int id = 0;
                    if(intent.getExtras() != null){
                        id = extras.getInt("buildingIdDirection");
                    }
                    if (id > 0) {
                        Building buildingChoose = null;
                        for (int i = 0; i < buildings.size(); i++) {
                            if (buildings.get(i).getId() == id) {
                                buildingChoose = buildings.get(i);
                                break;
                            }
                        }
                        if (location == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                            builder.setTitle("VNUMap");
                            builder.setMessage("Bạn vui lòng bật GPS");
                            builder.setPositiveButton("Đồng ý", null);
                            builder.show();
                        } else {
                            if (buildingChoose != null) {
                                String url = JSONMap.makeURL(location.getLatitude(), location.getLongitude(), buildingChoose.getLocation().getX()
                                        , buildingChoose.getLocation().getY());
                                DownloadAndDrawPath downloadAndDrawPath = new DownloadAndDrawPath(url);
                                downloadAndDrawPath.execute();
                            }
                        }
                    }


            }
        };
        handler.postDelayed(runnable, 2000);
        final FloatingActionButton fab_location = (FloatingActionButton) findViewById(R.id.fab_location);
        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                21.040987, 105.785605
//                21.036921, 105.781066
//              clear all path of direction
                if (path != null) {
                    path.remove();
                }

                if (location != null) {
                    //delete all marker
                    deleteMarker();
//                  if location in VNU
                    if (LEFT_BOTTOM_CONNER.latitude <= location.getLatitude() && location.getLatitude() <= RIGHT_TOP_CONNER.latitude
                            && LEFT_BOTTOM_CONNER.longitude <= location.getLongitude() && location.getLongitude() <= RIGHT_TOP_CONNER.longitude) {

//                      show buldings near location now
                        List<Building> buildings = ArrayBuildings.getInstance(MapsActivity.this).getBuildingsByLocation(numberofMarker, location);
                        addMarker(buildings);

//                      move camera to location
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                .zoom(ZOOM_CAMERA)
                                .tilt(60)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    } else {
//                      show marker with high priority

                        List<Building> buildings = ArrayBuildings.getInstance(MapsActivity.this).getBuildings(numberofMarker);
                        addMarker(buildings);

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
                                        .tilt(60)
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });
                        builder.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("VNUMap");
                    builder.setMessage("Bạn vui lòng bật GPS");
                    builder.setPositiveButton("Đồng ý", null);
                    builder.show();
                }
            }
        });
        fab_location.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_building);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, BuildingActivity.class);
                startActivity(intent);
//                finish();
            }
        });


        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                TextView textView = (TextView) findViewById(R.id.sliding_panel_txtViewName);
                RelativeLayout header = (RelativeLayout) findViewById(R.id.sliding_panel_header);
                TextView name = (TextView) findViewById(R.id.sliding_panel_txtViewName);
                TextView university = (TextView) findViewById(R.id.sliding_panel_txtViewUniversity);
                if (v > 0) {
                    header.setBackgroundColor(getResources().getColor(R.color.blue));
                    name.setTextColor(getResources().getColor(R.color.white));
                    university.setTextColor(getResources().getColor(R.color.white));
                } else {
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

        final EditText edtSearch = (EditText) findViewById(R.id.edtSearch);
        final ListView lstSeach = (ListView) findViewById(R.id.lstSearch);
        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                getSupportFragmentManager().findFragmentById(R.id.map).getView().setVisibility(View.INVISIBLE);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.list_search_layout);

                fab_location.setVisibility(View.INVISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
                slidingUpPanelLayout.setPanelHeight(0);
//  slidingUpPanelLayout.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                ImageButton menu = (ImageButton) findViewById(R.id.butMenu);
                menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
                isSearch = true;

                edtSearch.setFocusable(true);
                edtSearch.setFocusableInTouchMode(true);
                edtSearch.requestFocus();
                return false;
            }
        });

        edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    hideKeyboard(view);
                }
            }
        });

//      update data for list search
        final ArrayList<Building> buildings = new ArrayList<Building>();
        final ListSearchAdapter arrayAdapter = new ListSearchAdapter(MapsActivity.this,
                R.layout.list_item_search, buildings);
        lstSeach.setAdapter(arrayAdapter);
        lstSeach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                hideKeyboard();
                MapsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                MapsActivity.this.onBackPressed();
                focusAMarker(buildings.get(i));
                edtSearch.setFocusable(false);


            }

        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 1) {
                    List<Building> buildingList = SearchFilter.getInstance(MapsActivity.this).getResult(String.valueOf(charSequence));
                    buildings.clear();
                    for (int j = 0; j < buildingList.size(); j++) {
                        buildings.add(buildingList.get(j));
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//__________________________________________________________________________________________________
//set shadow menu
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

//create list menu left
        String[] list_menu_strings = getResources().getStringArray(R.array.list_menu);
        int[] list_menu_icon = {R.drawable.ic_settings, R.drawable.ic_info, R.drawable.ic_feedback};
        ArrayList<ListMenuAdapter.ItemMenu> menus = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            menus.add(new ListMenuAdapter.ItemMenu(list_menu_icon[i], list_menu_strings[i]));
        }
        final ListView listMenu = (ListView) findViewById(R.id.list_menu);
        listMenu.setAdapter(new ListMenuAdapter(this, R.layout.list_menu_item, menus));

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(MapsActivity.this, SettingActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent2 = new Intent(Intent.ACTION_SEND);
                        intent2.setType("message/rfc822");
                        intent2.putExtra(Intent.EXTRA_EMAIL, new String[]{"thinhtq_58@vnu.edu.vn"});
                        intent2.putExtra(Intent.EXTRA_SUBJECT, "[VNUMap]Đóng góp ý kiến");
                        intent2.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(intent2);
                        break;
                }
            }
        });

        ImageButton menu = (ImageButton) findViewById(R.id.butMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
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
                if (markerList.size() > 0) {
                    for (int i = 0; i < markerList.size(); i++) {
                        markerList.get(i).getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(SettingActivity.MENU_COLOR_FLOAT[
                                setting.getInt(SettingActivity.KEY_COLOR, 0)]));
                    }
                }
            }
        });

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

//      add Marker
        ArrayBuildings arrayBuildings = ArrayBuildings.getInstance(this);
        List<Building> buildings = arrayBuildings.getBuildings(numberofMarker);
        addMarker(buildings);

//      setting camera
        LatLng posCamera = new LatLng(LATITUDE_CAMERA, LONGGITUDE_CAMERA);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(posCamera)
                .zoom(ZOOM_CAMERA)
                .tilt(60)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < markerList.size(); i++) {
                    if (marker.equals(markerList.get(i).getMarker())) {
                        Building building = ArrayBuildings.getInstance(MapsActivity.this).getBuildingById(markerList.get(i).getId());
                        if (building != null) {
                            TextView name = (TextView) findViewById(R.id.sliding_panel_txtViewName);
                            name.setText(String.valueOf(building.getName()));
                            TextView university = (TextView) findViewById(R.id.sliding_panel_txtViewUniversity);
                            if (building.getUniversity() > 0) {
                                if (building.getUniversity() <= SearchFilter.UNIVERSITY_STRINGS.length) {
                                    university.setText(String.valueOf(SearchFilter.UNIVERSITY_STRINGS[building.getUniversity() - 1]));
                                } else {
                                    university.setText(String.valueOf(SearchFilter.UNIVERSITY_STRINGS[0]));
                                }
                            } else {
                                university.setText(String.valueOf(SearchFilter.UNIVERSITY_STRINGS[0]));
                            }
                        }
                        markerList.get(i).getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(SettingActivity.MENU_COLOR_FLOAT[
                                setting.getInt(SettingActivity.KEY_COLOR_CHOOSE, 3)]));
                        LatLng posCamera = new LatLng(markerList.get(i).getMarker().getPosition().latitude,
                                markerList.get(i).getMarker().getPosition().longitude);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(posCamera)
                                .zoom(ZOOM_CAMERA)
                                .tilt(60)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        slidingUpPanelLayout.setPanelHeight(140);

                    } else {
                        markerList.get(i).getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(SettingActivity.MENU_COLOR_FLOAT[
                                setting.getInt(SettingActivity.KEY_COLOR, 0)]));
                    }
                }
                return true;

            }
        });

        Bundle extras = intent.getExtras();
        if (extras != null) {
            int idBuildingFocus = extras.getInt("buildingId");
            Building buildingFocus = ArrayBuildings.getInstance(MapsActivity.this).getBuildingById(idBuildingFocus);
            if (buildingFocus != null) {
                deleteMarker();
                focusAMarker(buildingFocus);
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (isSearch) {
            getSupportFragmentManager().findFragmentById(R.id.map).getView().setVisibility(View.VISIBLE);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.list_search_layout);
            linearLayout.setVisibility(View.INVISIBLE);
            FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_location);
            fab1.setVisibility(View.VISIBLE);
            FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_building);
            fab2.setVisibility(View.VISIBLE);
            isSearch = false;
            ImageButton menu = (ImageButton) findViewById(R.id.butMenu);
            menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
//            MapsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            final EditText edtSearch = (EditText) findViewById(R.id.edtSearch);
            edtSearch.setFocusable(false);
        }
    }

    //  add Marker and set method on click to show sliding panel
    public void addMarker(List<Building> buildings) {
        if (buildings != null) {
            for (int i = 0; i < buildings.size(); i++) {
                LatLng latLng = new LatLng(buildings.get(i).getLocation().getX(), buildings.get(i).getLocation().getY());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(SettingActivity.MENU_COLOR_FLOAT[
                        setting.getInt(SettingActivity.KEY_COLOR, 0)])).title(buildings.get(i).getName()));
                markerList.add(new MapMarker(buildings.get(i).getId(), marker));
            }
        }
    }

    public void deleteMarker() {
        if (markerList.size() > 0) {
            for (int i = 0; i < markerList.size(); i++) {
                markerList.get(i).getMarker().setVisible(false);
            }
            markerList.clear();
        }
    }

    public void focusAMarker(Building building) {
        if (building != null) {
            deleteMarker();
            Location location = new Location("");
            location.setLatitude(building.getLocation().getX());
            location.setLongitude(building.getLocation().getY());
            List<Building> buildingList = ArrayBuildings.getInstance(MapsActivity.this).getBuildingsByLocation(numberofMarker, location);
            addMarker(buildingList);
            LatLng posCamera = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(posCamera)
                    .zoom(ZOOM_CAMERA)
                    .tilt(60)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            TextView name = (TextView) findViewById(R.id.sliding_panel_txtViewName);
            name.setText(String.valueOf(building.getName()));
            TextView university = (TextView) findViewById(R.id.sliding_panel_txtViewUniversity);
            if (building.getUniversity() > 0) {
                if (building.getUniversity() <= SearchFilter.UNIVERSITY_STRINGS.length) {
                    university.setText(String.valueOf(SearchFilter.UNIVERSITY_STRINGS[building.getUniversity() - 1]));
                } else {
                    university.setText(String.valueOf(SearchFilter.UNIVERSITY_STRINGS[0]));
                }
            } else {
                university.setText(String.valueOf(SearchFilter.UNIVERSITY_STRINGS[0]));
            }
        }
        slidingUpPanelLayout.setPanelHeight(140);
        if (markerList.size() > 0) {
            for (int k = 0; k < markerList.size(); k++) {
                if (markerList.get(k).getMarker().getPosition().latitude == building.getLocation().getX() &&
                        markerList.get(k).getMarker().getPosition().longitude == building.getLocation().getY()) {
                    markerList.get(k).getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(SettingActivity.MENU_COLOR_FLOAT[
                            setting.getInt(SettingActivity.KEY_COLOR_CHOOSE, 3)]));
                } else {
                    markerList.get(k).getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(SettingActivity.MENU_COLOR_FLOAT[
                            setting.getInt(SettingActivity.KEY_COLOR, 0)]));
                }
            }
        }

    }

    public void updateLocation() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            location = LocationServices.getInstance(MapsActivity.this).getLocation();
                            if (location != null) {
                                Log.d("HANDLE", String.valueOf(location.getLatitude()));
                                if (markerLocation != null) {
                                    markerLocation.setVisible(false);
                                }
                                if(circleLocation != null){
                                    circleLocation.setVisible(false);
                                }
                                markerLocation = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).title("FUCK"));
                                circleLocation = mMap.addCircle(new CircleOptions().center(markerLocation.getPosition())
                                        .fillColor(Color.parseColor("#161aa4dd")).strokeColor(Color.parseColor("#1aa4dd")).strokeWidth(4).radius(40));

//                              check when draw from building detail

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
    private void drawPath(String path) {
        try {
            JSONObject jsonObject = new JSONObject(path);
            Log.d("MAPDRAW", jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("routes");
            JSONObject routes = jsonArray.getJSONObject(0);
            JSONObject overview_polyline = routes.getJSONObject("overview_polyline");
            String polyline = overview_polyline.getString("points");
            List<LatLng> latLngs = JSONMap.decodePoly(polyline);
            this.path = mMap.addPolyline(new PolylineOptions().addAll(latLngs).width(13).color(Color.parseColor("#05b1fb")).geodesic(true));
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private class DownloadAndDrawPath extends AsyncTask<Void, Void, String> {
        private String url = null;

        DownloadAndDrawPath(String url) {
            this.url = url;
        }

        protected String doInBackground(Void... paramas) {
            JSONMap jsonMap = new JSONMap();
            String result = jsonMap.getJSONFromURL(url);
            Log.d("MAPDRAW", result);
            return result;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                drawPath(result);
            }
        }
    }


    private void hideKeyboard(View view) {
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}
