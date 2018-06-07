package com.github.timgoes1997.web.beans;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionBorder;
import com.github.timgoes1997.location.Location;
import com.github.timgoes1997.web.beans.dao.interfaces.RegionDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Stateless
public class RegionService {

    @Inject
    private RegionDAO regionDAO;

    public Region addRegionAsync(Region region) {
        Runnable r = () -> {
            System.out.println("Test");
        };
        try {

            r.wait();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Region addRegion(Region region) {
        if (regionDAO.exists(region.getName())) {
            throw new ClientErrorException(Response.Status.CONFLICT);
        }

        List<Region> regions = regionDAO.getAllRegions();
        List<Region> inRegions = new ArrayList<>();
        for (Region r : regions) {
            for (RegionBorder rb : region.getBorderList()) {
                if (r.isWithinRegion(rb.getX(), rb.getY())) {
                    inRegions.add(r);
                }
            }
        }

        return null;
    }

    public List<Region> getWithinRegions(Region region) {
        List<Region> regions = regionDAO.getAllRegions();
        Long regionId = (region.getId() != null) ? region.getId() : -1L;
        List<Region> withinRegions = new ArrayList<>();
        for (Region r : regions) {
            int amountInRegion = 0;
            if (!r.getId().equals(regionId)) {
                for (RegionBorder rb : r.getBorderList()) {
                    if (region.isWithinRegion(rb.getX(), rb.getY())) {
                        amountInRegion++;
                    }
                }
            }
            if (amountInRegion == r.getBorderList().size()) {
                withinRegions.add(r);
            }
        }
        return withinRegions;
    }


    public List<Region> getWithinRegions(Location location) {
        return getWithinRegions(location.getX(), location.getY());
    }

    /**
     * Gets all the regions the given point is in.
     *
     * @param x the x coord
     * @param y the y coord
     * @return the regions the given point is in.
     */
    public List<Region> getWithinRegions(double x, double y) {
        List<Region> regions = regionDAO.getAllRegions();
        List<Region> inRegions = new ArrayList<>();
        for (Region r : regions) {
            if (r.isWithinRegion(x, y)) {
                inRegions.add(r);
            }
        }
        return inRegions;
    }

    /**
     * Calculates which region the given point should get and returns the most recently added.
     *
     * @param x the x coord
     * @param y the y coord
     * @return the region the given point is in.
     */
    public Region getWithinRegion(double x, double y) {
        List<Region> within = getWithinRegions(x, y);
        if (within.size() <= 0) {
            return null;
        }
        Region mostRecentlyAdded = within.get(0);
        for (Region region : within) {
            if (!region.getId().equals(mostRecentlyAdded.getId())) {
                if (region.getAddedDate().after(mostRecentlyAdded.getAddedDate())) {
                    mostRecentlyAdded = region;
                }
            }
        }
        return mostRecentlyAdded;
    }
}
