package com.github.timgoes1997.entities;

import com.github.timgoes1997.location.Location;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity(name = "REGION")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BORDER_POINTS",
            joinColumns = {@JoinColumn(name = "REGION_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID")})
    private List<RegionBorder> borderList;

    public Region(String name) {
        this.name = name;
    }

    public Region() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RegionBorder> getBorderList() {
        List<RegionBorder> borders = borderList;
        Collections.sort(borders);
        return borders;
    }

    public void setBorderList(List<RegionBorder> borderList) {
        this.borderList = borderList;
    }


    //TODO: multithreaded implementation of calculating this, because this is pretty intensive

    /**
     * Kijkt of een locatie zich binnen een bepaalde regio bevind.
     * Gaat er vanuit dat de punten zich in een juiste volgorde bevinden, anders gaat het mis.
     * Moet daar waarschijnlijk nog een kleine verandering voor aanmaken aangezien dit wat accurater moet zijn.
     *
     * @param location
     * @return
     */
    public boolean isWithinRegion(Location location) {
        List<RegionBorder> regionBorders = getBorderList();

        if (regionBorders.size() <= 0) {
            return false;
        }

        double minX = regionBorders.get(0).getX();
        double maxX = regionBorders.get(0).getX();
        double minY = regionBorders.get(0).getY();
        double maxY = regionBorders.get(0).getY();

        for (RegionBorder r : regionBorders) {
            minX = Math.min(r.getX(), minX);
            maxX = Math.max(r.getX(), maxX);
            minY = Math.min(r.getY(), minY);
            maxY = Math.max(r.getY(), maxY);
        }

        //check if the location is outside of the boundries of all points.
        if (location.getX() < minX
                || location.getX() > maxX
                || location.getY() < minY
                || location.getY() > maxY) {
            return false;
        }

        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for (int i = 0, j = regionBorders.size() - 1; i < regionBorders.size(); j = i++) {
            if ((regionBorders.get(i).getY() > location.getY()) != (regionBorders.get(j).getY() > location.getY()) &&
                    location.getX() < (regionBorders.get(j).getX() - regionBorders.get(i).getX()) * (location.getY() - regionBorders.get(i).getY()) / (regionBorders.get(j).getY() - regionBorders.get(i).getY()) + regionBorders.get(i).getX()) {
                inside = !inside;
            }
        }

        return inside;
    }
}
