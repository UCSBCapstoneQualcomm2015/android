package com.sniffit.sniffit.Objects;

import java.io.Serializable;

/**
 * Created by andrewpang on 11/17/15.
 */
public class SniffitObject implements Serializable {

    String name;
    String _id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


}
