package com.github.timgoes1997.dummy;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionBorder;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DummyDataGenerator {

    private List<Region> regionList;
    private List<RegionRate> regionRates;

    public DummyDataGenerator(int amountOfRegions) {
        generate(amountOfRegions);
    }

    public DummyDataGenerator() {
        this(10);
    }

    private void generate(int amountOfRegions) {
        regionList = new ArrayList<>();
        regionRates = new ArrayList<>();

        //creating the rates
        for (int i = 0; i < amountOfRegions; i++) {
            regionList.add(new Region(String.format("region %s", i + 1)));
        }

        generateRegionRates();
        generateBorders();
    }

    private void generateRegionRates() {
        BigDecimal carStart = new BigDecimal(0.03d);
        BigDecimal carSteps = new BigDecimal(0.02d).setScale(3, BigDecimal.ROUND_HALF_UP);
        BigDecimal truckStart = new BigDecimal(0.07d);
        BigDecimal truckSteps = new BigDecimal(0.05d).setScale(3, BigDecimal.ROUND_HALF_UP);

        //Adding rates to the regions
        regionList.forEach(region -> {
            regionRates.addAll(generateRegionRateWeekDaysEnergyLabel(region, VehicleType.CAR, carStart, carSteps));
        });
        regionList.forEach(region -> {
            regionRates.addAll(generateRegionRateWeekDaysEnergyLabel(region, VehicleType.TRUCK, truckStart, truckSteps));
        });
    }

    private void generateBorders() {
        double borderX = 0.0d;
        double borderY = 0.0d;
        double borderStepsX = 5.0d;
        double borderStepsY = 5.0d;

        //Adding borderlocations to the regions
        for (Region region : regionList) {
            List<RegionBorder> borderList = new ArrayList<>();
            borderList.add(new RegionBorder(1L, borderX, borderY));
            borderList.add(new RegionBorder(2L, borderX + borderStepsX, borderY));
            borderList.add(new RegionBorder(3L, borderX + borderStepsX, borderY + borderStepsY));
            borderList.add(new RegionBorder(4L, borderX, borderY + borderStepsY));
            region.setBorderList(borderList);
            borderX += borderStepsX;
        }
    }

    private List<RegionRate> generateRegionRateWeekDaysEnergyLabel(Region region, VehicleType vehicleType, BigDecimal startPrice, BigDecimal steps) {
        List<RegionRate> regionRates = new ArrayList<>();
        BigDecimal currentPrice = startPrice.round(new MathContext(1));
        currentPrice = currentPrice.setScale(3, BigDecimal.ROUND_HALF_UP);
        for (EnergyLabel label : EnergyLabel.values()) {
            regionRates.addAll(generateRegionRateWeekDays(region, vehicleType, currentPrice, label));
            currentPrice = currentPrice.add(steps).setScale(3, BigDecimal.ROUND_HALF_UP);
        }
        return regionRates;
    }

    private List<RegionRate> generateRegionRateWeekDays(Region region, VehicleType vehicleType, BigDecimal price, EnergyLabel label) {
        List<RegionRate> regionRates = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            regionRates.add(new RegionRate(region, vehicleType, price, label, day, 0, 0, 23, 59));
        }
        return regionRates;
    }

    public List<RegionRate> getRatesForRegion(Region region) {
        List<RegionRate> rates = new ArrayList<>();
        for (RegionRate r : regionRates) {
            if (r.getRegion().equals(region)) {
                rates.add(r);
            }
        }
        return rates;
    }

    public List<Region> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region> regionList) {
        this.regionList = regionList;
    }

    public List<RegionRate> getRegionRates() {
        return regionRates;
    }

    public void setRegionRates(List<RegionRate> regionRates) {
        this.regionRates = regionRates;
    }
}
