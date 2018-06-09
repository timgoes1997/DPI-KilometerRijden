package com.github.timgoes1997.entities;

import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity(name = "RATE")
@NamedQueries({
        @NamedQuery(
                name = RegionRate.FIND_ALL,
                query = "SELECT r FROM RATE r"
        ),
        @NamedQuery(
                name = RegionRate.FIND_ID,
                query = "SELECT r FROM RATE r WHERE r.id = :id"
        ),
        @NamedQuery(
                name = RegionRate.FIND_BY_REGION,
                query = "SELECT r FROM RATE r WHERE r.region.id = :id"
        )
})
public class RegionRate {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "RegionRate.findAll";
    public static final String FIND_ID = "RegionRate.findByID";
    public static final String FIND_BY_REGION = "RegionRate.findByRegion";


    //======================
    //==      Fields      ==
    //======================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID")
    private Region region;

    @Digits(integer = 12, fraction = 6)
    @Column(name = "KILOMETER_PRICE")
    private BigDecimal kilometerPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENERGYLABEL")
    private EnergyLabel energyLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "DAY")
    private DayOfWeek dayOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "VEHICLE_TYPE")
    private VehicleType vehicleType;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME")
    private Calendar startTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_TIME")
    private Calendar endTime;

    public RegionRate(){

    }

    public RegionRate(Region region, VehicleType vehicleType, BigDecimal kilometerPrice, EnergyLabel energyLabel, DayOfWeek dayOfWeek, int startHour, int startMinute, int endHour, int endMinute) {
        this.region = region;
        this.kilometerPrice = kilometerPrice;
        this.energyLabel = energyLabel;
        this.dayOfWeek = dayOfWeek;
        this.vehicleType = vehicleType;

        if (startHour >= 24 || startHour < 0 || endHour >= 24 || endHour < 0) {
            throw new IllegalArgumentException("Hour should in the range of [0-23].");
        }

        if (startMinute >= 60 || startMinute < 0 || endMinute >= 60 || endMinute < 0) {
            throw new IllegalArgumentException("Hour should in the range of [0-59].");
        }

        this.startTime = GregorianCalendar.getInstance();
        startTime.set(2000, Calendar.JANUARY, 0, startHour, startMinute, 0);

        this.endTime = GregorianCalendar.getInstance();
        endTime.set(2000, Calendar.JANUARY, 0, endHour, endMinute, 0);
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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
