package com.github.timgoes1997.dummy;

import com.github.timgoes1997.entities.Region;
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

    public DummyDataGenerator() {
        generate();
    }

    private void generate() {
        regionList = new ArrayList<>();
        regionRates = new ArrayList<>();

        regionList.add(new Region("region 1"));
        regionList.add(new Region("region 2"));
        regionList.add(new Region("region 3"));
        regionList.add(new Region("region 4"));
        regionList.add(new Region("region 5"));
        regionList.add(new Region("region 6"));
        regionList.add(new Region("region 7"));
        BigDecimal carStart = new BigDecimal(0.03d);
        BigDecimal carSteps = new BigDecimal(0.02);
        BigDecimal truckStart = new BigDecimal(0.07d);
        BigDecimal truckSteps = new BigDecimal(0.05d);

        regionList.forEach(region -> {
            generateRegionRateWeekDaysEnergyLabel(region, VehicleType.CAR, carStart, carSteps);
        });
        regionList.forEach(region -> {
            regionRates.addAll(generateRegionRateWeekDaysEnergyLabel(region, VehicleType.TRUCK, truckStart, truckSteps));
        });
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
