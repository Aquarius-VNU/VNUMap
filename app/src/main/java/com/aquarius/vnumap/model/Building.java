package com.aquarius.vnumap.model;

import java.util.List;

/**
 * Created by Trac Quang Thinh on 09-Nov-15.
 */
public class Building {
    private int id;
    private String name;
    private List<String> rooms;
    private Point location;

    public Building(int id, String name, List<String> rooms, Point location){
        this.id = id;
        this.name = name;
        this.rooms = rooms;
        this.location = location;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Point getLocation(){
        return location;
    }

    public List<String> getRooms(){
        return rooms;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLocation(Point location){
        this.location = location;
    }

    public void setRooms(List<String> rooms){
        this.rooms = rooms;
    }
}
