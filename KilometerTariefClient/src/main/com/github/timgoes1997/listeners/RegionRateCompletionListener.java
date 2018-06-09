package com.github.timgoes1997.listeners;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.request.rate.RegionRateRequestType;
import com.github.timgoes1997.util.VisiblePanel;

import java.util.List;


public interface RegionRateCompletionListener {
    void onCompletion(List<RegionRate> regionRateList, RegionRateRequestType type);
    void onBack(VisiblePanel visiblePanel);
}
