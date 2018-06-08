package com.github.timgoes1997.listeners;

import com.github.timgoes1997.entities.RegionRate;

public interface RegionRatePanelListener {
    void onSelectCreation();
    void onSelectRegionRate(RegionRate regionRate);
    void onRegionRateUpdate(RegionRate regionRate);
    void onRegionRateDelete(RegionRate regionRate);
    void onRegionRateCreate(RegionRate regionRate);
}
