package com.aquarius.vnumap.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.vnumap.R;


import java.util.ArrayList;

/**
 * Created by Trac Quang Thinh on 29-Nov-15.
 */
public class ListMenuAdapter extends ArrayAdapter<ListMenuAdapter.ItemMenu> {
    private Activity context = null;
    private int layoutId = 0;
    private ArrayList<ItemMenu> menus;
    public ListMenuAdapter(Activity context, int layoutId, ArrayList<ItemMenu> menus){
        super(context, layoutId, menus);
        this.context = context;
        this.layoutId = layoutId;
        this.menus = menus;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        ImageView icon = (ImageView)convertView.findViewById(R.id.menu_list_icon);
        icon.setImageDrawable(convertView.getResources().getDrawable(menus.get(position).getIdImage()));
        TextView description = (TextView)convertView.findViewById(R.id.menu_list_text);
        description.setText(menus.get(position).getTextDescription());
        return convertView;
    }
    
//  object is a row in list menu
    public static class ItemMenu{
        private int idImage;
        private String textDescription;
        public ItemMenu(int idImage, String textDescription){
            this.idImage = idImage;
            this.textDescription = textDescription;
        }

    public int getIdImage() {
        return idImage;
    }

    public String getTextDescription() {
        return textDescription;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }
}
}
