package com.github.timgoes1997.web.beans.dao.interfaces;

import com.github.timgoes1997.entities.Region;

import java.util.List;

public interface RegionDAO {

    void create(Region region);
    void edit(Region region);
    void remove(Region region);

    boolean exists(String name);
    Region find(long id);
    Region find(String name);
    List<Region> getAllRegions();

}
