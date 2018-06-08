package com.github.timgoes1997.list;

import com.github.timgoes1997.entities.Region;

public class RegionListLine {

    private Region region;

    public RegionListLine(Region region){

    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "RegionListLine{" +
                "region=" + region +
                '}';
    }
}
