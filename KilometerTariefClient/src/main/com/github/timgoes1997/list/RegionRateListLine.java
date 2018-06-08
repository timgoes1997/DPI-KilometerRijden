package com.github.timgoes1997.list;

import com.github.timgoes1997.entities.RegionRate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegionRateListLine {

    private RegionRate regionRate;

    public RegionRateListLine(RegionRate regionRate) {
        this.regionRate = regionRate;
    }

    public RegionRate getRegionRate() {
        return regionRate;
    }

    public void setRegionRate(RegionRate regionRate) {
        this.regionRate = regionRate;
    }

    @Override
    public String toString() {
        return  regionRate.getVehicleType().toString() + " | "
                + regionRate.getDayOfWeek().toString() + ": "
                + String.valueOf(regionRate.getStartTime().get(Calendar.HOUR_OF_DAY)) + "-"
                + String.valueOf(regionRate.getEndTime().get(Calendar.HOUR_OF_DAY))
                + " - " + regionRate.getEnergyLabel().toString()
                + " - \u20ac" + regionRate.getKilometerPrice();
    }
}
