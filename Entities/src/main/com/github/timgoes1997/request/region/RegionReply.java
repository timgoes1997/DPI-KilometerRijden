package com.github.timgoes1997.request.region;

import com.github.timgoes1997.entities.Region;

public class RegionReply<OBJECT> {

    private Region region;

    private OBJECT object;

    public RegionReply(OBJECT object, Region region) {
        this.object = object;
        this.region = region;
    }

    public OBJECT getObject() {
        return object;
    }

    @Override
    public String toString() {
        return object.toString();
    }

}
