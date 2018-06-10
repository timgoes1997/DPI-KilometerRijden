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
import java.util.Objects;

@Entity(name = "RATE")
@NamedQueries({
        @NamedQuery(
                name = RegionRate.FIND_ALL,
                query = "SELECT r FROM RATE r ORDER BY r.id DESC"
        ),
        @NamedQuery(
                name = RegionRate.FIND_ID,
                query = "SELECT r FROM RATE r WHERE r.id = :id"
        ),
        @NamedQuery(
                name = RegionRate.FIND_BY_REGION,
                query = "SELECT r FROM RATE r WHERE r.region.id = :id ORDER BY r.id DESC"
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGION_ID")
    private Region region;

    @Digits(integer = 12, fraction = 6)
    @Column(name = "KILOMETER_PRICE", precision = 18, scale = 6)
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Calendar startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Calendar endTime;

    public RegionRate() {

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

        setStartTime(startHour, startMinute);
        setEndTime(endHour, endMinute);
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

    public void setStartTime(int startHour, int startMinute) {
        this.startTime = GregorianCalendar.getInstance();
        startTime.set(2000, Calendar.JANUARY, 0, startHour, startMinute, 0);
    }

    public void setEndTime(int endHour, int endMinute) {
        this.endTime = GregorianCalendar.getInstance();
        endTime.set(2000, Calendar.JANUARY, 0, endHour, endMinute, 0);
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


    public boolean isInRate(EnergyLabel energyLabel, VehicleType vehicleType) {
        //check labels
        if (vehicleType != this.vehicleType) return false;
        if (energyLabel != this.energyLabel) return false;

        //check day
        Calendar currentTime = GregorianCalendar.getInstance();
        int currentDayInt = currentTime.get(Calendar.DAY_OF_WEEK);
        DayOfWeek currentDay = DayOfWeek.of(currentDayInt);
        if (currentDay != this.dayOfWeek) return false;

        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);
        int startHour = startTime.get(Calendar.HOUR_OF_DAY);
        int startMinute = startTime.get(Calendar.MINUTE);
        int endHour = endTime.get(Calendar.HOUR_OF_DAY);
        int endMinute = endTime.get(Calendar.MINUTE);

        if (currentHour < startHour && currentHour > endHour) return false;
        if (currentHour == startHour && currentMinute < startMinute) return false;
        if (currentHour == endHour && currentMinute > endMinute) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionRate)) return false;
        RegionRate that = (RegionRate) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, region);
    }
}
