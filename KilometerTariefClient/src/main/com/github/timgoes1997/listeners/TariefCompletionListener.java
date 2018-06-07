package com.github.timgoes1997.listeners;

import com.github.timgoes1997.entities.RegionRate;

import java.util.List;

public interface TariefCompletionListener {
    void OnCompletion(List<RegionRate> regionRateList);
}
