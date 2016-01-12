package com.sniffit.sniffit.Objects;

/**
 * Created by sohanshah on 11/19/15.
 */
public class Snapdragon extends SniffitObject {


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    String ipAddress;
    Room room;
    String xCoord;
    String yCoord;

    public String getxCoord() {
        return xCoord;
    }

    public void setxCoord(String xCoord) {
        this.xCoord = xCoord;
    }

    public String getyCoord() {
        return yCoord;
    }

    public void setyCoord(String yCoord) {
        this.yCoord = yCoord;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }





}
