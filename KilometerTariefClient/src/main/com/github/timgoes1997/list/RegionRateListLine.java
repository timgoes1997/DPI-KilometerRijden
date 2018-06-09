package com.github.timgoes1997.list;

import com.github.timgoes1997.entities.RegionRate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionRateListLine)) return false;
        RegionRateListLine that = (RegionRateListLine) o;
        return Objects.equals(regionRate, that.regionRate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(regionRate);
    }

    @Override
    public String toString() {
        return  regionRate.getVehicleType().toString() + " | "
                + regionRate.getDayOfWeek().toString() + ": "
                + String.valueOf(regionRate.getStartTime().get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(regionRate.getStartTime().get(Calendar.MINUTE)) + " - "
                + String.valueOf(regionRate.getEndTime().get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(regionRate.getEndTime().get(Calendar.MINUTE))
                + " - " + regionRate.getEnergyLabel().toString()
                + " - \u20ac" + regionRate.getKilometerPrice().toString();
    }
}
