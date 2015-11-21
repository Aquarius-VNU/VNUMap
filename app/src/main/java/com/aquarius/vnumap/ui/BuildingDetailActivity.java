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
import com.aquarius.vnumap.model.Floors;

import java.util.ArrayList;

public class BuildingDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerFloorAdapter adapter;
    private ArrayList<Floors> listFloors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        ImageView thumbImage = (ImageView) findViewById(R.id.header);
        Bundle extras = this.getIntent().getExtras();
        int image = extras.getInt("image");
        thumbImage.setImageResource(image);

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
        listFloors.add(new Floors("Tầng 1"));
        listFloors.add(new Floors("Tầng 2"));
        listFloors.add(new Floors("Tầng 3"));
        listFloors.add(new Floors("Tầng 4"));
        listFloors.add(new Floors("Tầng 5"));
        listFloors.add(new Floors("Tầng 6"));
        listFloors.add(new Floors("Tầng 7"));
    }

}
