package com.aquarius.vnumap.model;

import java.util.List;

/**
 * Created by Trac Quang Thinh on 09-Nov-15.
 */
public class Building {
    private int id;
    private String name;
    private List<Room> rooms;
    private Point location;
    private int image;
    private int priority;

    public Building(int id, String name, List<Room> rooms, Point location, int priority) {
        this.id = id;
        this.name = name;
        this.rooms = rooms;
        this.location = location;
        this.image = 0;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public int getImage() {
        return image;
    }

    public int getPriority() {
        return priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}