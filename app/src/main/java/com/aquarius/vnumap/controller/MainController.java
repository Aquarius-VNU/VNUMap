package com.aquarius.vnumap.controller;

import android.util.Log;

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
* let's put 3 code line below to your activity
* InputStream inputStreamMap =  getResources().openRawResource(R.raw.map);
* InputStream inputStreamDirection =  getResources().openRawResource(R.raw.direction);
* List<Building> buildings = MainController.getListBuilding(inputStreamMap, inputStreamDirection);
* List buildings includes object Building what has the struct :
* id(int) - name(String) - rooms(List<Room>) - location(Point)
* To get data, you must use :
* getId() - getName()    - getRooms()          - getLocation()
* Object Room include name, floor, info and type with method get and set
* Inside type is type of room such as study room, toilet, lab...
* If a building hasn't any rooms, getRooms() return null
==================================================================================================*/
    public static List<Building> getListBuilding(InputStream inputStreamMap, InputStream inputStreamDirection){
        List<Building> list = null;
        try {
            if(inputStreamMap != null) {
                list = parse.parseMap(inputStreamMap);
                //join map and direction
                List<Room.Rooms> rooms = getRooms(inputStreamDirection);
                for(int i = 0 ; i < rooms.size(); i++) {
                    list.get(rooms.get(i).getId()-1).setRooms(rooms.get(i).getRooms());
                    Log.d("FUCK", String.valueOf(rooms.get(i).getId() + "-" + rooms.get(i).getRooms().get(0).getInfo()));
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }
        return list;
    }
    //provide list of rooms
    public static List<Room.Rooms> getRooms(InputStream inputStream){
        List<Room.Rooms> list = null;
        try{
            if(inputStream != null){
                list = parse.parseDirection(inputStream);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }catch(XmlPullParserException e){
            e.printStackTrace();
        }
        return list;
    }
}
