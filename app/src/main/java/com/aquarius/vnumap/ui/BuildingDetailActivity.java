package com.aquarius.vnumap.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.RecyclerFloorAdapter;
import com.aquarius.vnumap.model.Floors;

import java.util.ArrayList;

public class BuildingDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerFloorAdapter adapter;
    private ArrayList<Floors> listFloors;
    CollapsingToolbarLayout collapsingToolbar;
    int mutedColor = R.attr.colorPrimary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_direction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuildingDetailActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        createFloorList();
        adapter = new RecyclerFloorAdapter(this, listFloors);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void createFloorList(){
        listFloors = new ArrayList<Floors>();
        listFloors.add(new Floors("Tầng 1", R.drawable.ic_01));
        listFloors.add(new Floors("Tầng 2", R.drawable.ic_02));
        listFloors.add(new Floors("Tầng 3", R.drawable.ic_03));
        listFloors.add(new Floors("Tầng 4", R.drawable.ic_04));
        listFloors.add(new Floors("Tầng 5", R.drawable.ic_05));
        listFloors.add(new Floors("Tầng 6", R.drawable.ic_06));
        listFloors.add(new Floors("Tầng 7", R.drawable.ic_07));

    }
}
