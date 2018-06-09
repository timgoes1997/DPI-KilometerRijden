package com.github.timgoes1997.request.rate;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.request.RegionReplyType;

public class RegionRateReply {

    private RegionReplyType status;
    private RegionRate regionRate;

    public RegionRateReply(RegionRate regionRate) {
        this.status = RegionReplyType.SUCCEEDED;
        this.regionRate = regionRate;
    }

    public RegionRateReply(){
        this.status = RegionReplyType.CANCELED;
    }

    public RegionReplyType getStatus() {
        return status;
    }

    public RegionRate getRegionRate() {
        return regionRate;
    }
}
