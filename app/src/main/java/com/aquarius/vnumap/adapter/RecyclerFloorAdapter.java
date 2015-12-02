package com.aquarius.vnumap.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.controller.MainController;
import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.model.Floors;
import com.aquarius.vnumap.model.Room;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Thi Cam Van on 11/14/2015.
 */
public class RecyclerFloorAdapter extends RecyclerView.Adapter<RecyclerFloorAdapter.FloorViewHolder>{
    public Context context;
    private ArrayList<Floors> listFloors;
    private int expandedPosition = -1;
    private Activity activity;

    public RecyclerFloorAdapter(Context context, ArrayList<Floors> listFloors, Activity activity){
        this.context = context;
        this.listFloors = listFloors;
        this.activity = activity;
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
        List<Room> roomList = new ArrayList<Room>();
        roomList = floor.getRoomList();
        holder.roomList = roomList;

        GridRoomAdapter adapter = new GridRoomAdapter(holder.itemView.getContext(), roomList, activity);
        holder.gridView.setAdapter(adapter);

        holder.tvFloor.setText(floor.getName());
        //holder.titleFloor.setText(floor.getName());

        if (position == expandedPosition) {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.image_expand_toggle.setImageResource(R.drawable.circle_minus);
            holder.tvFloor.setTextSize(20);
            //holder.titleFloor.setText("");
        } else {
            holder.linearLayout.setVisibility(View.GONE);
            holder.image_expand_toggle.setImageResource(R.drawable.circle_plus);
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
        public TextView titleFloor;
        private LinearLayout linearLayout;
        public ImageView image_expand_toggle;
        private GridView gridView;
        private List<Room> roomList = new ArrayList<Room>();
        private OnItemClickListener clickListener;
        FloorViewHolder(View itemView){
            super(itemView);
            tvFloor = (TextView) itemView.findViewById(R.id.floor_name);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.expanded_linear_layout);

            gridView = (GridView) itemView.findViewById(R.id.gridExpandArea);
            //titleFloor = (TextView) itemView.findViewById(R.id.titleFloor);
            image_expand_toggle = (ImageView) itemView.findViewById(R.id.img_expand_toggle);
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

