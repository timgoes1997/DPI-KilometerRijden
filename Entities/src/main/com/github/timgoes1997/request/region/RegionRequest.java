package com.github.timgoes1997.request.region;

public class RegionRequest {

    private RegionRequestType regionRequestType;

    public RegionRequest(RegionRequestType regionRequestType) {
        this.regionRequestType = regionRequestType;
    }

    public RegionRequestType getRegionRequestType() {
        return regionRequestType;
    }
}
