package com.aquarius.vnumap.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.ui.BuildingDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen Thi Cam Van on 11/18/2015.
 */
public class BuildingCardAdapter extends RecyclerView.Adapter<BuildingCardAdapter.ViewHolder> {

    List<Building> mItems;
    Bundle bundle = new Bundle();
    private Context context;

    public BuildingCardAdapter(Context context, List<Building> buildingList) {
        super();
        this.context = context;
        mItems = buildingList;

//        mItems = new ArrayList<Building>();
//        Building building = new Building();
//        building.setName("Khoa ......");
//        building.setDescription("This is description");
//        building.setImage(R.drawable.baltoro_glacier);
//        mItems.add(building);
//
//        building = new Building();
//        building.setName("Khoa ........");
//        building.setDescription("This is description");
//        building.setImage(R.drawable.great_barrier_reef);
//        mItems.add(building);
//
//        building = new Building();
//        building.setName("Khoa .........");
//        building.setDescription("This is description");
//        building.setImage(R.drawable.aurora_borealis);
//        mItems.add(building);
//
//        building = new Building();
//        building.setName("Khoa ........");
//        building.setDescription("This is description");
//        building.setImage(R.drawable.grand_canyon);
//        mItems.add(building);
//
//        building = new Building();
//        building.setName("Khoa ........");
//        building.setDescription("This is description");
//        building.setImage(R.drawable.oto);
//        mItems.add(building);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Building floor = mItems.get(i);
        viewHolder.tvBuilding.setText(floor.getName());
        viewHolder.tvDesBuilding.setText(floor.getDescription());
        viewHolder.imgThumbnail.setImageResource(floor.getImage());
        int image = floor.getImage();
        bundle = new Bundle();
        bundle.putInt("image", image);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView tvBuilding;
        public TextView tvDesBuilding;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            tvBuilding = (TextView)itemView.findViewById(R.id.tv_building);
            tvDesBuilding = (TextView)itemView.findViewById(R.id.tv_des_building);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent (context, BuildingDetailActivity.class);
                    i.putExtras(bundle);
                    context.startActivity(i);
                }
            });
        }
    }
}


