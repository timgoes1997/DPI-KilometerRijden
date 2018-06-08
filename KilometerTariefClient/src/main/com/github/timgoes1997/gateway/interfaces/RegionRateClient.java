package com.github.timgoes1997.gateway.interfaces;

import com.github.timgoes1997.entities.RegionRate;

public interface RegionRateClient {
    void create(RegionRate rate);
    void update(RegionRate rate);
    void delete(RegionRate rate);

}
