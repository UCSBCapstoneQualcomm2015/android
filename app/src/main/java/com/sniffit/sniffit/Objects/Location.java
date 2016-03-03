package com.sniffit.sniffit.Objects;

import java.io.Serializable;

/**
 * Created by sohanshah on 2/8/16.
 */
public class Location implements Serializable{

    public void setyCoord(String yCoord) {
        this.yCoord = yCoord;
    }

    public void setxCoord(String xCoord) {
        this.xCoord = xCoord;
    }

    String yCoord;
    String xCoord;

    public String getxCoord() {
        return xCoord;
    }


    public String getyCoord() {
        return yCoord;
    }

}
