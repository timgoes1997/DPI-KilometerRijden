package com.github.timgoes1997.listeners;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;

public interface RegionRateClientGatewayListener {
    void onReceiveRegions(Region region);
    void onReceiveRegionRates(RegionRate regionRate);
}
