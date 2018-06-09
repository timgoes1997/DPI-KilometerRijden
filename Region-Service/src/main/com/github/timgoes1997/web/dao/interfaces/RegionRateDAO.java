package com.github.timgoes1997.web.dao.interfaces;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;

import java.util.List;

public interface RegionRateDAO {

    void create(RegionRate regionRate);
    void edit(RegionRate regionRate);
    void remove(RegionRate regionRate);

    boolean exists(long id);
    RegionRate find(long id);
    List<RegionRate> findRates(Region region);
}
