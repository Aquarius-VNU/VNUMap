package com.aquarius.vnumap.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.RecyclerFloorAdapter;
import com.aquarius.vnumap.controller.MainController;
import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.model.Floors;
import com.aquarius.vnumap.model.Room;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BuildingDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerFloorAdapter adapter;
    private ArrayList<Floors> listFloors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        ImageView thumbImage = (ImageView) findViewById(R.id.header);
        InputStream inputStreamMap =  getResources().openRawResource(R.raw.map);
        InputStream inputStreamDirection =  getResources().openRawResource(R.raw.direction);
        List<Building> buildings = MainController.getListBuilding(inputStreamMap, inputStreamDirection);

        int i = this.getIntent().getIntExtra("building", 0);
        // building = buildings.get(i-1);
        Building building = new Building();
        for(int k = 0; k < buildings.size(); k++){
            if(buildings.get(k).getId() == i){
                building = buildings.get(k);
            }
        }
//        thumbImage.setImageResource(building.getImage());

        thumbImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(), building.getImage(), 720, 405));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(building.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_direction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuildingDetailActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        listFloors = new ArrayList<>();
        List<Room> roomList = new ArrayList<Room>();
        roomList = building.getRooms();
        int maxFloor = 0;
        for (int k = 0; k < roomList.size()- 1; k++){
            maxFloor = Math.max(roomList.get(k).getFloor(), roomList.get(k+1).getFloor());
        }
        for(int j = 0; j < maxFloor; j++){
            listFloors.add(new Floors("Tầng " + (j + 1)));
            for(int k = 0; k < roomList.size(); k++){
                Room room = roomList.get(k);
                if(room.getFloor() == j+1){
                    listFloors.get(j).getRoomList().add(room);
                }
            }
        }

        adapter = new RecyclerFloorAdapter(this, listFloors, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
