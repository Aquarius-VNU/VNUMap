package com.aquarius.vnumap.adapter;

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
public class GridRoomAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    private Context context;
    private List<Room> roomList;
    public GridRoomAdapter(Context context, List<Room> roomList){
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

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.header = (ImageView) convertView.findViewById(R.id.header);
            viewHolder.direction = (TextView) convertView.findViewById(R.id.detail_direction);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Room room = roomList.get(position);
       // viewHolder.ivIcon.setImageDrawable(room.icon);
        viewHolder.tvTitle.setText(room.getName());
        //viewHolder.direction.setText(room.getInfo());
        //viewHolder.direction.setVisibility(View.VISIBLE);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public ImageView header;
        public TextView direction;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
