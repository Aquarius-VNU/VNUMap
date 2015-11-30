package com.aquarius.vnumap.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.model.Room;

import java.util.List;


/**
 * Created by Nguyen Thi Cam Van on 11/24/2015.
 */
public class GridRoomAdapter extends BaseAdapter{
    private Context context;
    private List<Room> roomList;
    private Activity activity;
    int mark = 0;
    public GridRoomAdapter(Context context, List<Room> roomList, Activity activity){
        this.context = context;
        this.roomList = roomList;
    }

    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int i) {
        return roomList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        mark = position;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.header = (ImageView) convertView.findViewById(R.id.header);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Room room = roomList.get(position);
        viewHolder.tvTitle.setText(room.getName());
        viewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ABC", Toast.LENGTH_LONG).show();
                View contextView = LayoutInflater.from(activity).inflate(R.layout.activity_building_detail, null);
                TextView direction = (TextView) contextView.findViewById(R.id.detail_direction);
                //ImageView image = (ImageView) contextView.findViewById(R.id.header);
                //image.setImageResource(R.drawable.image04);
                direction.setVisibility(View.VISIBLE);
                direction.setText("ABC");
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public ImageView header;
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int i = R.id.building_detail_activity;
        View contextView = activity.getLayoutInflater().inflate(R.layout.activity_building_detail, null);
        TextView direction = (TextView) contextView.findViewById(R.id.detail_direction);
        ImageView image = (ImageView) contextView.findViewById(R.id.header);
        image.setImageResource(R.drawable.image04);
        Toast.makeText(view.getContext(), "ABC", Toast.LENGTH_LONG).show();
        //direction.setVisibility(View.VISIBLE);
        //direction.setText("ABC");
    }*/
}
