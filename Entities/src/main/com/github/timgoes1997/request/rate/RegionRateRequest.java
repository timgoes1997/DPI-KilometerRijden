package com.github.timgoes1997.request.rate;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;

public class RegionRateRequest {
    private RegionRate regionRate;
    private RegionRateRequestType regionRateRequestType;

    public RegionRateRequest(RegionRate regionRate, RegionRateRequestType regionRateRequestType) {
        this.regionRate = regionRate;
        this.regionRateRequestType = regionRateRequestType;
    }

    public RegionRate getRegionRate() {
        return regionRate;
    }

    public RegionRateRequestType getRegionRateRequestType() {
        return regionRateRequestType;
    }
}
