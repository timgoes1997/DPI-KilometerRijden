package com.github.timgoes1997.dummy;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionBorder;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DummyDataGenerator {

    private List<Region> regionList;
    private List<RegionRate> regionRates;

    public DummyDataGenerator(int amountOfRegions) {
        generate(amountOfRegions);
    }

    public DummyDataGenerator(){
        this(10);
    }

    private void generate(int amountOfRegions) {
        regionList = new ArrayList<>();
        regionRates = new ArrayList<>();

        //creating the rates
        for(int i = 0; i < amountOfRegions; i++){
            regionList.add(new Region(String.format("region %s", i)));
        }

        generateRegionRates();
        generateBorders();
    }

    private void generateRegionRates(){
        BigDecimal carStart = new BigDecimal(0.03d);
        BigDecimal carSteps = new BigDecimal(0.02);
        BigDecimal truckStart = new BigDecimal(0.07d);
        BigDecimal truckSteps = new BigDecimal(0.05d);

        //Adding rates to the regions
        regionList.forEach(region -> {
            generateRegionRateWeekDaysEnergyLabel(region, VehicleType.CAR, carStart, carSteps);
        });
        regionList.forEach(region -> {
            regionRates.addAll(generateRegionRateWeekDaysEnergyLabel(region, VehicleType.TRUCK, truckStart, truckSteps));
        });
    }

    private void generateBorders(){
        double borderX = 0.0d;
        double borderY = 0.0d;
        double borderStepsX = 5.0d;
        double borderStepsY = 5.0d;

        //Adding borderlocations to the regions
        for (Region region: regionList) {
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
        BigDecimal currentPrice = startPrice;
        for (EnergyLabel label : EnergyLabel.values()) {
            regionRates.addAll(generateRegionRateWeekDays(region, vehicleType, currentPrice, label));
            currentPrice.add(steps);
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

}
