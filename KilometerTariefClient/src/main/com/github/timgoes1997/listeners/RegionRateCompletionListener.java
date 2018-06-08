package com.github.timgoes1997.listeners;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.util.VisiblePanel;


public interface RegionRateCompletionListener {
    void onCompletion(RegionRate regionRate);
    void onBack(VisiblePanel visiblePanel);
}
