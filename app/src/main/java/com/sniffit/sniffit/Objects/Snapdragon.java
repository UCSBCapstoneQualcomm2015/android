package com.sniffit.sniffit.Objects;

import com.sniffit.sniffit.Objects.SniffitObject;

/**
 * Created by sohanshah on 11/19/15.
 */
public class Snapdragon extends SniffitObject {


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    String ip;
    Room room;
    String x;
    String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }





}
