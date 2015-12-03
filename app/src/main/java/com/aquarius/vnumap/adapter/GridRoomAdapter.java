package com.aquarius.vnumap.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        this.activity = activity;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        mark = position;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.grid_item);
            //viewHolder.header = (ImageView) convertView.findViewById(R.id.header);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Room room = roomList.get(position);

        for(int i = 0; i < roomList.size(); i++){

            switch (room.getType()){
                case 2: //WC
                    viewHolder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_restroom));
                    break;
                case 3: //Lab
                    viewHolder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_computer));
                    break;
                case 4: //Tro giang
                    viewHolder.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_support));
                    break;
                default: //Study room
                    break;
            }
        }
        final TextView direction = (TextView) activity.findViewById(R.id.detail_direction);
        direction.setVisibility(View.GONE);
        viewHolder.tvTitle.setText(room.getName());
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)activity.findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitleEnabled(true);
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "ABC", Toast.LENGTH_LONG).show();
//                View contextView = View.inflate(activity, R.layout.activity_building_detail, null);
                // ImageView image = (ImageView) activity.findViewById(R.id.header);
                // image.setImageResource(R.drawable.image04);
                direction.setVisibility(View.VISIBLE);
                direction.setText(room.getInfo());
                CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)activity.findViewById(R.id.toolbar_layout);
                toolbarLayout.setTitleEnabled(false);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public RelativeLayout relativeLayout;
        //public ImageView header;
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
