package com.switchak.switchak;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muham on 06/02/2018.
 */

class Room {


    //Variables
    private String roomId;
    private String roomName;
    private boolean power;
    private List<Long> times = new ArrayList<>();
    private List<Float> readings = new ArrayList<>();
    private List<Timestamp> timestampList = new ArrayList<>();


    //Constructor that takes room name
    Room(String roomId) {
        this.roomId = roomId;
    }

    public Room(String roomId, String roomName, boolean power) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.power = power;
    }

    //Getters and setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Timestamp> getTimestampList() {
        return timestampList;
    }

    public void setTimestampList(List<Timestamp> timestampList) {
        this.timestampList = timestampList;
    }

    public List<Long> getTimes() {
        return times;
    }

    public void setTimes(List<Long> times) {
        this.times = times;
    }

    public List<Float> getReadings() {
        return readings;
    }

    public void setReadings(List<Float> readings) {
        this.readings = readings;
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }
}