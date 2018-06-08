package com.github.timgoes1997.entities;

import com.github.timgoes1997.location.Location;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(name = "REGION")
@NamedQueries({
        @NamedQuery(
                name = Region.FIND_ALL,
                query = "SELECT r FROM REGION r"
        ),
        @NamedQuery(
                name = Region.FIND_ID,
                query = "SELECT r FROM REGION r WHERE r.id = :id"
        ),
        @NamedQuery(
                name = Region.FIND_NAME,
                query = "SELECT r FROM REGION r WHERE r.name = :name"
        )
})
public class Region {

    //======================
    //==    Constansts    ==
    //======================

    public static final String FIND_ALL = "Region.findAll";
    public static final String FIND_ID = "Region.findByID";
    public static final String FIND_NAME = "Region.findByName";


    //======================
    //==      Fields      ==
    //======================

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

    @Temporal(TemporalType.DATE)
    @Column(name="DATE")
    private Date addedDate;


    public Region(String name) {
        this.name = name;
        this.addedDate = new Date();
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

    public Date getAddedDate() {
        return addedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Region)) return false;
        Region region = (Region) o;
        return Objects.equals(id, region.id) &&
                Objects.equals(name, region.name) &&
                Objects.equals(addedDate, region.addedDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, addedDate);
    }


    public boolean isWithinRegion(Location location){
        return isWithinRegion(location.getX(), location.getY());
    }

//TODO/OPTIONAL: multithreaded implementation of calculating this, because this can be pretty intensive
    /**
     * Kijkt of een locatie zich binnen een bepaalde regio bevind.
     * Gaat er vanuit dat de punten zich in een juiste volgorde bevinden, anders gaat het mis.
     *
     * @param locX x location
     * @param locY y location
     * @return if it's withing the current region.
     */
    public boolean isWithinRegion(double locX, double locY) {
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
        if (locX < minX
                || locX > maxX
                || locY < minY
                || locY > maxY) {
            return false;
        }

        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for (int i = 0, j = regionBorders.size() - 1; i < regionBorders.size(); j = i++) {
            if ((regionBorders.get(i).getY() > locY) != (regionBorders.get(j).getY() > locY) &&
                    locX < (regionBorders.get(j).getX() - regionBorders.get(i).getX()) * (locY - regionBorders.get(i).getY()) / (regionBorders.get(j).getY() - regionBorders.get(i).getY()) + regionBorders.get(i).getX()) {
                inside = !inside;
            }
        }

        return inside;
    }
}
