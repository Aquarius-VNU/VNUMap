package com.aquarius.vnumap.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.vnumap.R;

import java.util.Objects;

/**
 * Created by DangDo on 11/15/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String>{
    private Context context;
    private String[] imgNames;
    private Integer[] imgIds;
    private LayoutInflater inflater = null;


    public CustomListAdapter(Activity context, String[] imgNames, Integer[] imgIds){
        super(context, R.layout.card_view, imgNames);
        this.context = context;
        this.imgIds = imgIds;
        this.imgNames = imgNames;
        this.inflater = LayoutInflater.from(context);
    }



    public View getView(int position, View view, ViewGroup parent){

        View cardView = inflater.inflate(R.layout.card_view, null);

        TextView textView = (TextView) cardView.findViewById(R.id.image_name);
        textView.setText(imgNames[position]);

        ImageView imageView = (ImageView) cardView.findViewById(R.id.img);
        imageView.setImageResource(imgIds[position]);

        return cardView;
    }
}
