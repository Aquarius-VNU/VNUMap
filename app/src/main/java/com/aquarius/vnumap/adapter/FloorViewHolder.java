package com.aquarius.vnumap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.vnumap.R;

/**
 * Created by Nguyen Thi Cam Van on 11/14/2015.
 */
public class FloorViewHolder extends RecyclerView.ViewHolder{
    private Context context;
    public TextView tvFloor;
    public ImageView imgFloor;
    FloorViewHolder(Context context, View itemView){
        super(itemView);
        this.context = context;
        tvFloor = (TextView) itemView.findViewById(R.id.floor_name);
        //imgFloor = (ImageView) itemView.findViewById(R.id.imgFloor);
    }

}
