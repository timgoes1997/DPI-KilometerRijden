package com.github.timgoes1997.web.beans;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionBorder;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.location.Location;
import com.github.timgoes1997.web.beans.endpoint.RegionInformer;
import com.github.timgoes1997.web.beans.endpoint.RegionRateInformer;
import com.github.timgoes1997.web.dao.interfaces.RegionDAO;
import com.github.timgoes1997.web.dao.interfaces.RegionRateDAO;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Stateless
public class RegionService {

    @Inject
    private RegionDAO regionDAO;

    @Inject
    private RegionRateDAO regionRateDAO;

    @Inject
    @RegionRateInformer
    private Event<RegionRate> regionRateEvent;

    @Inject
    @RegionInformer
    private Event<Region> regionEvent;

    public boolean regionExists(Region region) {
        return regionDAO.exists(region.getName());
    }

    public List<Region> getAllRegions() {
        return regionDAO.getAllRegions();
    }

    public RegionRate findRegionRate(long id) {
        return regionRateDAO.find(id);
    }

    public void addRegionRate(RegionRate regionRate) {
        if (regionRate.getRegion() == null) {
            throw new NotAcceptableException("Tried to add Regionrate without specifying a region");
        }

        if (!regionDAO.exists(regionRate.getRegion().getId())) {
            throw new ClientErrorException(Response.Status.NOT_FOUND);
        }

        regionRateDAO.create(regionRate);
        regionRateEvent.fire(regionRate);
    }

    public void addRegionRateNoCheck(RegionRate regionRate) {
        regionRateDAO.create(regionRate);
    }

    public void updateRegionRate(RegionRate regionRate) {
        if (regionRate.getRegion() == null) {
            throw new NotAcceptableException("Tried to add Regionrate without specifying a region");
        }

        if (!regionDAO.exists(regionRate.getRegion().getId())) {
            throw new NotFoundException();
        }

        if (!regionRateDAO.exists(regionRate.getId())) {
            throw new NotFoundException();
        }

        RegionRate update = regionRateDAO.find(regionRate.getId());
        regionRateDAO.edit(update);
        regionRateEvent.fire(regionRate);
    }

    public void removeRegionRate(RegionRate regionRate) {
        if (!regionRateDAO.exists(regionRate.getId())) {
            throw new NotFoundException();
        }

        RegionRate remove = regionRateDAO.find(regionRate.getId());
        regionRateDAO.remove(remove);
    }

    public void addRegion(Region region) {
        if (regionDAO.exists(region.getName())) {
            throw new ClientErrorException(Response.Status.CONFLICT);
        }

        /*
        List<Region> regions = regionDAO.getAllRegions();
        List<Region> inRegions = new ArrayList<>();
        for (Region r : regions) {
            for (RegionBorder rb : region.getBorderList()) {
                if (r.isWithinRegion(rb.getX(), rb.getY())) {
                    inRegions.add(r);
                }
            }
        }*/

        regionDAO.create(region);
        regionEvent.fire(region);
    }

    public void addRegionNoCheck(Region region) {
        regionDAO.create(region);
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

    public List<RegionRate> getRegionRates(Region region) {
        //TODO: Filter region rates, only pick the most recently added in a given timeframe
        return regionRateDAO.findRates(region);
    }

    public RegionRate getRegionRate(Location location, VehicleType vehicleType, EnergyLabel energyLabel) {
        return getRegionRate(getWithinRegion(location.getX(), location.getY()), vehicleType, energyLabel);
    }

    public RegionRate getRegionRate(Region region, VehicleType vehicleType, EnergyLabel energyLabel) {
        if (region == null) return null;

        List<RegionRate> regionRates = getRegionRates(region);
        RegionRate currentRegionRate = null;
        for (RegionRate regionRate : regionRates) {
            if (regionRate.isInRate(energyLabel, vehicleType)) {
                if (currentRegionRate == null || currentRegionRate.getId() < regionRate.getId()) {
                    currentRegionRate = regionRate;
                }
            }
        }
        return currentRegionRate;
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
