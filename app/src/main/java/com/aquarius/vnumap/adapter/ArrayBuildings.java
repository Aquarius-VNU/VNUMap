package com.aquarius.vnumap.adapter;

import android.content.Context;
import android.location.Location;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.controller.MainController;
import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.model.Point;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Trac Quang Thinh on 17-Nov-15.
 */
/*
* Class is a array include building object
* It will sort building that depends priority attribute
* Class include method Search
* After search, result will include MAXSIZE - 1 and 1 elements of search result
* get by location, id
* */
public class ArrayBuildings {
    private static ArrayBuildings ourInstance = null;

    private static ArrayList<Building> buildingArray;

    public static ArrayBuildings getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new ArrayBuildings(context);
        }
        return ourInstance;
    }

    private ArrayBuildings(Context context) {
        buildingArray = new ArrayList<>();
        List<Building> buildings = MainController.getListBuilding(context.getResources().openRawResource(R.raw.map), context.getResources().openRawResource(R.raw.direction));
        for(int i = 0 ; i < buildings.size() ; i++){
            buildingArray.add(buildings.get(i));
        }
//      sort depend priority
        Collections.sort(buildingArray, new Comparator<Building>() {
            @Override
            public int compare(Building building1, Building building2) {
                if(building1.getPriority() > building2.getPriority()){
                    return 1;
                }else if(building1.getPriority() == building2.getPriority()){
                    return building1.getName().compareTo(building2.getName());
                }else{
                    return -1;
                }
            }
        });
    }

    public List<Building> getBuildings(int size){
        List<Building> buildings = new ArrayList<>();
        if(size > buildingArray.size() || size <= 0){
            size = buildingArray.size();
        }
        for(int i = 0 ; i < size ; i++){
            buildings.add(buildingArray.get(i));
        }
        return  buildings;
    }

    public List<Building> getBuildingsByLocation(int size, Location location){
        List<Building> buildings = new ArrayList<>();
        if(size > buildingArray.size() || size <= 0){
            return buildingArray;
        }else{
            for(int i = 0 ; i < buildingArray.size() ; i++){
                float[] result = new float[1];
                Point element = buildingArray.get(i).getLocation();
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), element.getX(), element.getY(), result);
                if(buildings.size() < size && result[0] <= 40){
                    buildings.add((buildingArray.get(i)));
                }
            }
        }
        if(buildings.size() < size){
            for(int i = 0 ; i < buildingArray.size() ; i++){
                boolean checkLike = false;
                for(int j = 0 ; j < buildings.size() ; j++){
                    if(buildings.get(j).getId() == buildingArray.get(i).getId()){
                        checkLike = true;
                        break;
                    }
                }
                if(!checkLike && buildings.size() < size){
                    buildings.add(buildingArray.get(i));
                }
            }
        }
        return buildings;
    }

    public Building getBuildingById(int id){
        for(int i = 0 ; i < buildingArray.size() ; i++){
            if(buildingArray.get(i).getId() == id){
                return buildingArray.get(i);
            }
        }
        return null;
    }
}
