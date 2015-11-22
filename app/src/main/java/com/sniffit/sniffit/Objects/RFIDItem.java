package com.sniffit.sniffit.Objects;

import com.sniffit.sniffit.Objects.SniffitObject;

/**
 * Created by sohanshah on 11/18/15.
 */
public class RFIDItem extends SniffitObject {

    String id;
    Float x;
    Float y;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }


}
