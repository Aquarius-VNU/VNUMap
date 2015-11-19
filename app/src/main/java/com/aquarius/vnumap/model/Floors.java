package com.aquarius.vnumap.model;

import java.util.ArrayList;

/**
 * Created by Nguyen Thi Cam Van on 11/14/2015.
 */
public class Floors {
    private String name;
    private int idImage;
    public Floors(String name){
        this.name = name;
    }

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

    public void setName(String name){
        this.name = name;
    }

    public void setIdImage(int id){
        this.idImage = id;
    }

}