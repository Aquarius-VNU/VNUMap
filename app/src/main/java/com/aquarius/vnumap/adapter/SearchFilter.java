package com.aquarius.vnumap.adapter;

import android.content.Context;

import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.ui.MapsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Trac Quang Thinh on 29-Nov-15.
 * Class define method to provide list search from string
 * Depend name, keyword, university
 * String input store :
 * a part of name : +2
 * a part of keyword : +0.5
 * a part of university : +0.7
 */
public class SearchFilter {
    private static final double POINT_NAME = 2;
    private static final double POINT_KEYWORD = 1;
    private static final double POINT_UNIVERSITY = 1;

    public static final String[] UNIVERSITY_STRINGS={"Đại học Quốc Gia Hà Nội", "Đại học Công Nghệ - ĐHQG Hà Nội",
            "Đại học Ngoại Ngữ - ĐHQG Hà Nội", "Khoa Luật - ĐHQG Hà Nội", "Khoa Y Dược - ĐHQG Hà Nội",
            "Khoa Quốc Tế - ĐHQG Hà Nội", "Đại học Giáo Dục - ĐHQG Hà Nội", "Đại học Kinh Tế - ĐHQG Hà Nội", "Đại học Sư Phạm Hà Nội"};

    private static SearchFilter instance = null;
//  store all of elements from buildings in map.xml
    private static List<Building> buildings = null;

    public static SearchFilter getInstance(Context context) {
        if(instance == null){
            instance = new SearchFilter(context);
        }
        return instance;
    }

    private SearchFilter(Context context) {
//      get all elements
        buildings = ArrayBuildings.getInstance(context).getBuildings(0);
    }

//  provide list of result, sort follow point A->Z
    public List<Building> getResult(final String searchInput){

        List<Building> result = new ArrayList<>();
        for(int i = 0 ; i < buildings.size() ; i++){
            if(getPoint(buildings.get(i), searchInput) > 0){
                result.add(buildings.get(i));
            }
        }
        Collections.sort(result, new Comparator<Building>() {
            @Override
            public int compare(Building building, Building t1) {
                if(getPoint(building, searchInput) > getPoint(t1, searchInput)){
                    return 1;
                }else if(getPoint(building, searchInput) == getPoint(t1, searchInput)){
                    return 0;
                }else {
                    return -1;
                }
            }
        });
        return result;
    }

//  provide point of building at index with input searchInput
    private static double getPoint(Building building, String searchInput){
        double point = 0;
        String[] inputs = searchInput.split(" ");
            for (int i = 0; i < inputs.length; i++) {
//              name store search input
                if (building.getName().toLowerCase().contains(inputs[i].toLowerCase())){
                    point += POINT_NAME;
                }
//              university store search input
                if(building.getUniversity() > 0 && building.getUniversity() <= UNIVERSITY_STRINGS.length){
                    String university = UNIVERSITY_STRINGS[building.getUniversity() - 1];
                    if(university.toLowerCase().contains(inputs[i].toLowerCase())){
                        point += POINT_UNIVERSITY;
                    }
                }
//              keyword store search input
                if(building.getKeyword() != null) {
                    if (building.getKeyword().toLowerCase().contains(inputs[i].toLowerCase())) {
                        point += POINT_KEYWORD;
                    }
                }
            }

        return point;
    }
}
