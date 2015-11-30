package com.aquarius.vnumap.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.model.Building;

import java.util.ArrayList;

/**
 * Created by Trac Quang Thinh on 29-Nov-15.
 */
public class ListSearchAdapter extends ArrayAdapter<Building> {
    private Activity context = null;
    private int layoutId = 0;
    private ArrayList<Building> buildings;
    public ListSearchAdapter(Activity context, int layoutId, ArrayList<Building> buildings){
        super(context, layoutId, buildings);
        this.context = context;
        this.layoutId = layoutId;
        this.buildings = buildings;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        TextView name = (TextView)convertView.findViewById(R.id.list_search_txtViewName);
        name.setText(String.valueOf(buildings.get(position).getName()));
        TextView university = (TextView)convertView.findViewById(R.id.list_search_txtViewUniversity);
        university.setText(SearchFilter.UNIVERSITY_STRINGS[buildings.get(position).getUniversity() - 1]);
        return convertView;
    }
}
