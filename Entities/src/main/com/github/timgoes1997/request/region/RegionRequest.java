package com.github.timgoes1997.request.region;

import com.github.timgoes1997.entities.Region;

public class RegionRequest {

    private Region region;
    private RegionRequestType regionRequestType;

    public RegionRequest(RegionRequestType regionRequestType) {
        this.regionRequestType = regionRequestType;
    }

    public RegionRequest(Region region, RegionRequestType regionRequestType) {
        this.region = region;
        this.regionRequestType = regionRequestType;
    }

    public RegionRequestType getRegionRequestType() {
        return regionRequestType;
    }

    public Region getRegion() {
        return region;
    }
}
