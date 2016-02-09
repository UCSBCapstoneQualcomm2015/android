package com.sniffit.sniffit.Objects;

import java.io.Serializable;

/**
 * Created by sohanshah on 2/8/16.
 */
public class Location implements Serializable{

    String yCoord;
    String xCoord;

    public String getxCoord() {
        return xCoord;
    }


    public String getyCoord() {
        return yCoord;
    }

}
