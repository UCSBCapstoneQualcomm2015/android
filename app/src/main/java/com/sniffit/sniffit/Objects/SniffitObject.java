package com.sniffit.sniffit.Objects;

import java.io.Serializable;

/**
 * Created by andrewpang on 11/17/15.
 */
public class SniffitObject implements Serializable {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
