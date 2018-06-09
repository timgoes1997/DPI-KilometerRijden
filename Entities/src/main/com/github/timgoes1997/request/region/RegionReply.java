package com.github.timgoes1997.request.region;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.request.RegionReplyType;

public class RegionReply<OBJECT> {

    private Region region;
    private OBJECT object;
    private RegionReplyType status;

    /**
     * Creates a succeeded reply
     * @param object the object given
     * @param region
     */
    public RegionReply(OBJECT object, Region region) {
        this.object = object;
        this.region = region;
        this.status = RegionReplyType.SUCCEEDED;
    }

    /**
     * Creates a canceled reply
     */
    public RegionReply(){
        this.status = RegionReplyType.CANCELED;
    }

    public OBJECT getObject() {
        return object;
    }

    @Override
    public String toString() {
        return object.toString();
    }

}
