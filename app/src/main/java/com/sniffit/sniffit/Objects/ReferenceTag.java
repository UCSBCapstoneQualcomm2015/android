package com.sniffit.sniffit.Objects;

/**
 * Created by sohanshah on 11/26/15.
 */
public class ReferenceTag extends  SniffitObject{

    Room room;
    String tagId;
    String xCoord;
    String yCoord;


    public String getX() {
        return xCoord;
    }

    public void setX(String x) {
        this.xCoord = x;
    }

    public String getY() {
        return yCoord;
    }

    public void setY(String y) {
        this.yCoord = y;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getId() {
        return tagId;
    }

    public void setId(String id) {
        this.tagId = id;
    }


}
