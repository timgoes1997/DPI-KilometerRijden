package com.github.timgoes1997.request.region;

import com.github.timgoes1997.entities.Region;

public class RegionRequestRegionRates extends RegionRequest {

    private Region region;

    public RegionRequestRegionRates(RegionRequestType regionRequestType, Region region) {
        super(regionRequestType);
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }
}
