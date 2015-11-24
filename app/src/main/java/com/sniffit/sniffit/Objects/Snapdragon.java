package com.sniffit.sniffit.Objects;

import com.sniffit.sniffit.Objects.SniffitObject;

/**
 * Created by sohanshah on 11/19/15.
 */
public class Snapdragon extends SniffitObject {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    String name;
    String ip;



}
