package com.github.timgoes1997.entities;

import javax.persistence.*;

@Entity(name = "REGION_BORDER")
public class RegionBorder implements  Comparable<RegionBorder> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_ID")
    private Long regionInsertionPointId;

    @Column(name="X")
    private double x;

    @Column(name="Y")
    private double y;

    public RegionBorder(){

    }

    public RegionBorder(Long regionInsertionPointId, double x, double y) {
        this.regionInsertionPointId = regionInsertionPointId;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * To ensure that it gets inserted right into the database
     * @return
     */
    public Long getRegionInsertionPointId() {
        return regionInsertionPointId;
    }

    @Override
    public int compareTo(RegionBorder o) {
        if(this.getRegionInsertionPointId() > o.getRegionInsertionPointId()){
            return 1;
        }else if(this.getRegionInsertionPointId() < o.getRegionInsertionPointId()){
            return -1;
        }else{
            return 0;
        }
    }
}
