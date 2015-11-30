package com.aquarius.vnumap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.ExpandableListAdapter;
import com.aquarius.vnumap.controller.MainController;
import com.aquarius.vnumap.model.Building;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DangDo on 11/30/2015.
 */
public class OtherBuildingFragment extends Fragment {
    private ExpandableListAdapter adapter;
    private ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<Building>> listDataChild;


    public OtherBuildingFragment (){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.other_building_layout, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.other_building_list_view);

        prepareListData();

        adapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Intent i = new Intent(getContext(), MapsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("buildingId", adapter.getChild(groupPosition, childPosition).getId());
                startActivity(i);
                return false;
            }
        });



        return view;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Building>>();

        InputStream inputStreamMap =  getResources().openRawResource(R.raw.map);
        InputStream inputStreamDirection =  getResources().openRawResource(R.raw.direction);
        List<Building> buildings = MainController.getListBuilding(inputStreamMap, inputStreamDirection);

        // Adding child data
        listDataHeader.add("Quán ăn uống");
        listDataHeader.add("ATM");
        listDataHeader.add("Tình yêu");
        listDataHeader.add("In ấn");
        listDataHeader.add("Nhà xe");
        listDataHeader.add("Khác");

        // Adding child data
        List<Building> foodAndDrink = new ArrayList<Building>();
        List<Building> atm = new ArrayList<Building>();
        List<Building> love = new ArrayList<Building>();
        List<Building> photocopy = new ArrayList<Building>();
        List<Building> parking = new ArrayList<Building>();
        List<Building> other = new ArrayList<Building>();


        for(int i = 0; i < buildings.size(); i++){
            switch (buildings.get(i).getType()){
                case 3: foodAndDrink.add(buildings.get(i));
                    break;
                case 4: atm.add(buildings.get(i));
                    break;
                case 5: photocopy.add(buildings.get(i));
                    break;
                case 6: parking.add(buildings.get(i));
                    break;
                case 7: love.add(buildings.get(i));
                    break;
                case 8: other.add(buildings.get(i));
                    break;
            }
        }


        listDataChild.put(listDataHeader.get(0), foodAndDrink); // Header, Child data
        listDataChild.put(listDataHeader.get(1), atm);
        listDataChild.put(listDataHeader.get(2), love);
        listDataChild.put(listDataHeader.get(3), photocopy);
        listDataChild.put(listDataHeader.get(4), parking);
        listDataChild.put(listDataHeader.get(5), other);

    }
}
