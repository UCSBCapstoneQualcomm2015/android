package com.sniffit.sniffit.Objects;

/**
 * Created by andrewpang on 3/1/16.
 */
public class History extends SniffitObject{

    String roomId;
    String yCoord;
    String xCoord;
    String tagId;
    String userId;
    String created_at;

    public History(String roomId, String yCoord, String xCoord, String tagId, String userId, String created_at) {
        this.roomId = roomId;
        this.yCoord = yCoord;
        this.xCoord = xCoord;
        this.tagId = tagId;
        this.userId = userId;
        this.created_at = created_at;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
