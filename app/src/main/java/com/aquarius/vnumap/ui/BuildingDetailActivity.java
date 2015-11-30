package com.aquarius.vnumap.ui;

import android.content.Intent;
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
        Building building = buildings.get(i - 1);
        thumbImage.setImageResource(building.getImage());

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


}
