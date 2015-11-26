package com.aquarius.vnumap.adapter;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Trac Quang Thinh on 26-Nov-15.
 */
public class MapMarker {
    private Marker marker;
//  id of building in map.xml
    private int id;

    public MapMarker(int id, Marker marker){
        this.id = id;
        this.marker = marker;
    }

    public int getId() {
        return id;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
