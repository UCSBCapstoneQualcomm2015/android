package com.sniffit.sniffit.Objects;

import com.google.gson.annotations.SerializedName;
import com.sniffit.sniffit.Objects.SniffitObject;

/**
 * Created by sohanshah on 11/18/15.
 */
public class RFIDItem extends SniffitObject {

    String tagId;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return this.name;
    }


}