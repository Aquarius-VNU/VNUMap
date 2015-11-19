package com.aquarius.vnumap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.model.Floors;

import java.util.ArrayList;

/**
 * Created by Nguyen Thi Cam Van on 11/14/2015.
 */
public class RecyclerFloorAdapter extends RecyclerView.Adapter<RecyclerFloorAdapter.FloorViewHolder>{
    private Context context;
    private ArrayList<Floors> listFloors;
    private int expandedPosition = -1;
    public RecyclerFloorAdapter(Context context, ArrayList<Floors> listFloors){
        this.context = context;
        this.listFloors = listFloors;
    }

    @Override
    public FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_list_floors_layout, parent, false);
        FloorViewHolder viewHolder = new FloorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FloorViewHolder holder, int position) {
        Floors floor = listFloors.get(position);
        holder.tvFloor.setText(floor.getName());

        if (position == expandedPosition) {
            holder.gridLayout.setVisibility(View.VISIBLE);
        } else {
            holder.gridLayout.setVisibility(View.GONE);
        }
        holder.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                // Check for an expanded view, collapse if you find one
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                // Set the current position to "expanded"
                expandedPosition = position;
                notifyItemChanged(expandedPosition);

                Toast.makeText(context, "#" + (position+1) + " - " + listFloors.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFloors.size();
    }

    public static class FloorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvFloor;
        public ImageView imgFloor;
        private GridLayout gridLayout;
        private OnItemClickListener clickListener;
        FloorViewHolder(View itemView){
            super(itemView);
            tvFloor = (TextView) itemView.findViewById(R.id.floor_name);
            gridLayout = (GridLayout) itemView.findViewById(R.id.gridExpandArea);
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

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
}

