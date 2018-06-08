package com.github.timgoes1997.request.rate;

import com.github.timgoes1997.entities.RegionRate;

public class RegionRateReply {

    private RegionRateReplyType status;
    private RegionRate regionRate;

    public RegionRateReply(RegionRate regionRate, RegionRateReplyType status) {
        this.status = status;
        this.regionRate = regionRate;
    }

    public RegionRateReplyType getStatus() {
        return status;
    }

    public RegionRate getRegionRate() {
        return regionRate;
    }
}
