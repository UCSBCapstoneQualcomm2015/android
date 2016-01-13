package com.sniffit.sniffit.Objects;

/**
 * Created by sohanshah on 11/26/15.
 */
public class ReferenceTag extends  SniffitObject{

    Room room;
    String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
