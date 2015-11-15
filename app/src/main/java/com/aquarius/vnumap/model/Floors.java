package com.aquarius.vnumap.model;

/**
 * Created by Nguyen Thi Cam Van on 11/14/2015.
 */
public class Floors {
    private String name;
    private int idImage;
    public Floors(String name, int idImage){
        this.name = name;
        this.idImage = idImage;
    }
    public String getName(){
        return name;
    }

    public int getIdImage(){
        return idImage;
    }

}