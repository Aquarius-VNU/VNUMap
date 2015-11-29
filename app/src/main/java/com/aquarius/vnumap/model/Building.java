package com.aquarius.vnumap.model;

import com.aquarius.vnumap.R;

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
    private String description;
    private int type;
    private int university;
    private String keyword;

    private static final Integer[] imageId = {
            0, R.drawable.image01, 0, 0, R.drawable.image04,
            R.drawable.image05, 0, R.drawable.image07, 0, 0,
            0, R.drawable.image11, 0, 0, R.drawable.image14,
            R.drawable.image15, 0, 0, 0, R.drawable.image19,
            R.drawable.image20, 0, R.drawable.image22, R.drawable.image23, R.drawable.image24,
            R.drawable.image25, R.drawable.image26, R.drawable.image27, R.drawable.image28, R.drawable.image29,
            0, 0, R.drawable.image32, 0, 0,
            R.drawable.image35
    };

    public Building(){

    }

    public Building(int id, String name, List<Room> rooms, Point location, int priority, int type, int university, String keyword) {
        this.id = id;
        this.name = name;
        this.rooms = rooms;
        this.location = location;
        this.image = imageId[id];
        this.priority = priority;
        this.type = type;
        this.university = university;
        this.keyword = keyword;
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

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public int getUniversity() {
        return university;
    }

    public String getKeyword() {
        return keyword;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUniversity(int university) {
        this.university = university;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}