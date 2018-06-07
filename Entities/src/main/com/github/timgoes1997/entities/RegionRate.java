package com.github.timgoes1997.entities;

import com.github.timgoes1997.entities.enums.EnergyLabel;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity(name = "RATE")
public class RegionRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="REGION_ID")
    private Region region;

    @Digits(integer = 12, fraction = 2)
    @Column(name = "KILOMETER_PRICE")
    private BigDecimal kilometerPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENERGYLABEL")
    private EnergyLabel energyLabel;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME")
    private Calendar startTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_TIME")
    private Calendar endTime;

    public RegionRate(Region region, BigDecimal kilometerPrice, EnergyLabel energyLabel, Calendar startTime, Calendar endTime) {
        this.region = region;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public RegionRate(Region region, BigDecimal kilometerPrice, EnergyLabel energyLabel, int startHour, int startMinute, int endHour, int endMinute) {
        this.region = region;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;

        if(startHour >=24 || startHour < 0 || endHour >=24 || endHour < 0){
            throw new IllegalArgumentException("Hour should in the range of [0-23].");
        }

        if(startMinute >= 60 || startMinute < 0 || endMinute >= 60 || endMinute < 0){
            throw new IllegalArgumentException("Hour should in the range of [0-60].");
        }

        this.startTime = GregorianCalendar.getInstance();
        startTime.set(0, 0, 0, startHour, startMinute, 0);

        this.endTime = GregorianCalendar.getInstance();
        endTime.set(0,0 ,0 ,endHour , endMinute,0 );
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public BigDecimal getKilometerPrice() {
        return kilometerPrice;
    }

    public void setKilometerPrice(BigDecimal kilometerPrice) {
        this.kilometerPrice = kilometerPrice;
    }

    public EnergyLabel getEnergyLabel() {
        return energyLabel;
    }

    public void setEnergyLabel(EnergyLabel energyLabel) {
        this.energyLabel = energyLabel;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }
}
