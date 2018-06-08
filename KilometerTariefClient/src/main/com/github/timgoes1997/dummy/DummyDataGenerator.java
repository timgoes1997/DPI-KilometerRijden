package com.github.timgoes1997.dummy;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DummyDataGenerator {

    private List<Region> regionList;
    private List<RegionRate> regionRates;

    public DummyDataGenerator(){
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
        regionRates.addAll(generateRegionWeek(regionList.get(0), new BigDecimal(0.1d), EnergyLabel.A));



    }

    private List<RegionRate> generateRegionWeek(Region region, BigDecimal price, EnergyLabel label){
        List<RegionRate> regionRates = new ArrayList<>();
        for(DayOfWeek day : DayOfWeek.values()){
            regionRates.add(new RegionRate(region, price, label, day, 0, 0, 23, 59));
        }
        return regionRates;
    }

}
