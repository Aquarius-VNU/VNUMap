package com.aquarius.vnumap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.model.Floors;

import java.util.ArrayList;

/**
 * Created by Nguyen Thi Cam Van on 11/14/2015.
 */
public class RecyclerFloorAdapter extends RecyclerView.Adapter<FloorViewHolder>{
    private Context context;
    private ArrayList<Floors> listFloors;
    public RecyclerFloorAdapter(Context context, ArrayList<Floors> listFloors){
        this.context = context;
        this.listFloors = listFloors;
    }

    @Override
    public FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_list_floors_layout, parent, false);
        FloorViewHolder viewHolder = new FloorViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FloorViewHolder viewHolder, int position) {
        Floors floor = listFloors.get(position);
        viewHolder.tvFloor.setText(floor.getName());
        //viewHolder.imgFloor.setImageResource(floor.getIdImage());
    }

    @Override
    public int getItemCount() {
        return listFloors.size();
    }
}
