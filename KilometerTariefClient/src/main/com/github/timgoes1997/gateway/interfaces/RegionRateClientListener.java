package com.github.timgoes1997.gateway.interfaces;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.rate.RegionRateRequestType;

import java.util.List;

public interface RegionRateClientListener {
    void onReceiveRegions(List<Region> regions);
    void onReceiveRegionRates(List<RegionRate> regionRates);
    void onClientRequestCanceled(RegionRateRequest request);

    void onReceiveRegionRateCreate(RegionRate regionRate);
    void onReceiveRegionRateUpdate(RegionRate regionRate);
    void onReceiveRegionRateRemove(RegionRate regionRate);
}
