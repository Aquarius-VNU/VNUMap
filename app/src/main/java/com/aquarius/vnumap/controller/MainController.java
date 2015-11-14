package com.aquarius.vnumap.controller;

import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.model.Room;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Trac Quang Thinh on 09-Nov-15.
 */
public class MainController {
    private static XmlParse parse = new XmlParse();
/*==================================================================================================
* how to use getListBuilding
* map is moved to res/raw
* let's put 2 code line below to your activity
* InputStream inputStream =  getResources().openRawResource(R.raw.map);
* List<Building> buildings = MainController.getListBuilding(inputStream);
* List buildings includes object Building what has the struct :
* id(int) - name(String) - rooms(List<String>) - location(Point)
* To get data, you must use :
* getId() - getName()    - getRooms()          - getLocation()
==================================================================================================*/
    public static List<Building> getListBuilding(InputStream inputStream){
        List<Building> list = null;
        try {
            if(inputStream != null) {
                list = parse.parseMap(inputStream);
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }
        return list;
    }

    public  static List<Room.Rooms> getRooms(InputStream inputStream){
        List<Room.Rooms> list = null;
        try{
            if(inputStream != null){
                list = parse.parseDirection(inputStream);
            }
        }catch(IOException e) {

        }catch(XmlPullParserException e){

        }

        return list;
    }
}
