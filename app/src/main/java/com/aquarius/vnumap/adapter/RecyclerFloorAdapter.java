package com.aquarius.vnumap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.model.Floors;
import com.aquarius.vnumap.ui.BuildingDetailActivity;
import com.aquarius.vnumap.ui.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by Nguyen Thi Cam Van on 11/14/2015.
 */
public class RecyclerFloorAdapter extends RecyclerView.Adapter<RecyclerFloorAdapter.FloorViewHolder>{
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
    public void onBindViewHolder(final FloorViewHolder viewHolder, int position) {
        Floors floor = listFloors.get(position);
        viewHolder.tvFloor.setText(floor.getName());
        //viewHolder.imgFloor.setImageResource(floor.getIdImage());
        viewHolder.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(context, "#" + position + " - " + listFloors.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFloors.size();
    }

    public static class FloorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Context context;
        public TextView tvFloor;
        public ImageView imgFloor;
        private OnItemClickListener clickListener;
        FloorViewHolder(Context context, View itemView){
            super(itemView);
            this.context = context;
            tvFloor = (TextView) itemView.findViewById(R.id.floor_name);
            //imgFloor = (ImageView) itemView.findViewById(R.id.imgFloor);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(OnItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getPosition());
        }

    }
}

