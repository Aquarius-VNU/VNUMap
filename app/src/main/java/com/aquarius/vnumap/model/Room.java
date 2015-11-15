package com.aquarius.vnumap.model;

import java.util.List;

/**
 * Created by Trac Quang Thinh on 13-Nov-15.
 */
public class Room {
    private String name;
    private int floor;
    private String info;

    public Room(String name, int floor, String info){
        this.name = name;
        this.floor = floor;
        this.info = info;
    }

    public String getName(){
        return this.name;
    }

    public int getFloor(){
        return this.floor;
    }

    public String getInfo(){
        return this.info;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFloor(int floor){
        this.floor = floor;
    }

    public void setInfo(String info){
        this.info = info;
    }
//==================================================================================================
    public static class Rooms{
        List<Room> rooms;
        int id;

        public  Rooms(int id, List<Room> rooms){
            this.id = id;
            this.rooms = rooms;
        }

        public int getId(){
            return this.id;
        }

        public List<Room> getRooms(){
            return this.rooms;
        }
    }
}
