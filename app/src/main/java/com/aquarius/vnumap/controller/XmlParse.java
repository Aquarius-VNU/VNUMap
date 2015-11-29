package com.aquarius.vnumap.controller;

import android.support.annotation.Nullable;
import android.util.Xml;

import com.aquarius.vnumap.model.Building;
import com.aquarius.vnumap.model.Point;
import com.aquarius.vnumap.model.Room;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trac Quang Thinh on 09-Nov-15.
 */
public class XmlParse {
    private static final String NAME_SPACE = null;
    public static final int PARSE_MAP_CHOICE = 1;
    //read text
    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException{
        String result = "";
        if(parser.next() == XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return  result;
    }

    //  read tag id
    public int readId(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "id");
        String id = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "id");
        return  Integer.valueOf(id);
    }

    //  read tag name
    public String readName(XmlPullParser parser) throws  XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "name");
        return name;
    }

    //  read tag x
    public double readX(XmlPullParser parser) throws  XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "x");
        String x = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "x");
        return Double.valueOf(x);
    }

    //  read tag y
    public double readY(XmlPullParser parser) throws  XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "y");
        String y = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "y");
        return Double.valueOf(y);
    }

    public int readPriority(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "priority");
        String priority = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "priority");
        return Integer.valueOf(priority);
    }

    public int readType(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "type");
        String type = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "type");
        if (type.equals("")){
            return 0;
        }
        return Integer.valueOf(type);
    }

    public int readUniversity(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "university");
        String university = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "university");
        if(university.equals("")){
            return 0;
        }
        return Integer.valueOf(university);
    }

    public String readKeyword(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "keyword");
        String keyword = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "keyword");
        if(keyword.equals("")){
            return null;
        }
        return keyword;
    }

    public Building readBuilding(XmlPullParser parser) throws  XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG,NAME_SPACE, "building");
        int id = 0;
        String name = null;
        double x = 0.0;
        double y = 0.0;
        int priority = 0;
        int type = 0;
        int university = 0;
        String keyword = null;

        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String nameParse = parser.getName();
            if(nameParse.equals("id")){
                id = readId(parser);
            }else if(nameParse.equals("name")){
                name = readName(parser);
            }else if(nameParse.equals("x")){
                x = readX(parser);
            }else if(nameParse.equals("y")){
                y = readY(parser);
            }else if(nameParse.equals("priority")){
                priority = readPriority(parser);
            }else if(nameParse.equals("type")){
                type = readType(parser);
            }else if(nameParse.equals("university")){
                university = readUniversity(parser);
            }else if(nameParse.equals("keyword")){
                keyword = readKeyword(parser);
            }
            else{
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "building");
        return new Building(id, name, null, new Point(x, y), priority, type, university, keyword);
    }

    public List<Building> readMap(XmlPullParser xmlPullParser) throws  XmlPullParserException, IOException{
        List<Building> buildings = new ArrayList();
        xmlPullParser.require(XmlPullParser.START_TAG, NAME_SPACE, "map");
        while(xmlPullParser.next() != XmlPullParser.END_TAG){
            if(xmlPullParser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = xmlPullParser.getName();
            if(name.equals("building")){
                buildings.add(readBuilding(xmlPullParser));
            }else{
                skip(xmlPullParser);
            }
        }
        return buildings;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException{
        if(parser.getEventType() != XmlPullParser.START_TAG){
            throw  new IllegalStateException();
        }
        int depth = 1;
        while(depth != 0){
            switch (parser.next()){
                case XmlPullParser.START_TAG: depth++; break;
                case XmlPullParser.END_TAG : depth--; break;
            }
        }
    }

    public List<Building> parseMap(InputStream inputStream) throws XmlPullParserException, IOException{
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            // don't use namespace
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            xmlPullParser.nextTag();
            return readMap(xmlPullParser);
        }finally {
            inputStream.close();
        }
    }
//==================================================================================================
//  read tag room
    public Room readRoom(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "room");
        String name = parser.getAttributeValue(NAME_SPACE, "name");
        int floor = Integer.valueOf(parser.getAttributeValue(NAME_SPACE, "floor"));
        int type = Integer.valueOf(parser.getAttributeValue(NAME_SPACE, "type"));
        String info = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "room");
    return new Room(name, floor, info, type);
}

    //  read tag rooms
    public Room.Rooms readRooms(XmlPullParser parser) throws  XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "rooms");
        List<Room> rooms = new ArrayList<>();
        int id = Integer.valueOf(parser.getAttributeValue(NAME_SPACE, "id"));
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("room")){
                    rooms.add(readRoom(parser));
            }else{
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "rooms");
        return new Room.Rooms(id, rooms);
    }

    public List<Room.Rooms> readDirection(XmlPullParser parser) throws XmlPullParserException, IOException{
        List<Room.Rooms> rooms = new ArrayList<>();
        int id = 0;
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "direction");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();
            if(name.equals("rooms")){
                rooms.add(readRooms(parser));

            }else{
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, "direction");
        return rooms;
    }

    public List<Room.Rooms> parseDirection(InputStream inputStream) throws XmlPullParserException, IOException{
        try{
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            xmlPullParser.nextTag();
            return readDirection(xmlPullParser);
        }finally {
            inputStream.close();
        }
    }
}
