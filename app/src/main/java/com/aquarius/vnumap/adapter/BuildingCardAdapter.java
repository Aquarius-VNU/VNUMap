package com.aquarius.vnumap.adapter;

import android.content.Context;
import android.content.Intent;
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
    private Context context;

    public BuildingCardAdapter(Context context) {
        super();
        this.context = context;
        mItems = new ArrayList<Building>();
        Building building = new Building();
        building.setName("Khoa ......");
        building.setDescription("This is description");
        building.setImage(R.drawable.baltoro_glacier);
        mItems.add(building);

        building = new Building();
        building.setName("Khoa ........");
        building.setDescription("This is description");
        building.setImage(R.drawable.great_barrier_reef);
        mItems.add(building);

        building = new Building();
        building.setName("Khoa .........");
        building.setDescription("This is description");
        building.setImage(R.drawable.aurora_borealis);
        mItems.add(building);

        building = new Building();
        building.setName("Khoa ........");
        building.setDescription("This is description");
        building.setImage(R.drawable.grand_canyon);
        mItems.add(building);

        building = new Building();
        building.setName("Khoa ........");
        building.setDescription("This is description");
        building.setImage(R.drawable.oto);
        mItems.add(building);
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
        Building nature = mItems.get(i);
        viewHolder.tvNature.setText(nature.getName());
        viewHolder.tvDesNature.setText(nature.getDescription());
        viewHolder.imgThumbnail.setImageResource(nature.getImage());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView tvNature;
        public TextView tvDesNature;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            tvNature = (TextView)itemView.findViewById(R.id.tv_nature);
            tvDesNature = (TextView)itemView.findViewById(R.id.tv_des_nature);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, BuildingDetailActivity.class));
                }
            });
        }
    }
}


