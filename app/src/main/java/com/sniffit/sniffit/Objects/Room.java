package com.sniffit.sniffit.Objects;

import com.sniffit.sniffit.Objects.SniffitObject;

/**
 * Created by andrewpang on 11/17/15.
 */
public class Room extends SniffitObject {

    String length;
    String width;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
