package com.github.timgoes1997.listeners;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.util.VisiblePanel;

public interface RegionRatePanelListener {
    void onSelectCreation();
    void onSelectRegionRate(RegionRate regionRate);
    void onRegionRateUpdate(RegionRate regionRate);
    void onRegionRateDelete(RegionRate regionRate);
    void onRegionRateCreate(RegionRate regionRate);
    void onBack(VisiblePanel visiblePanel);
}
