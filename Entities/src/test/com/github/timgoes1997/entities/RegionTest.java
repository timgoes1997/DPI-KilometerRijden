package com.github.timgoes1997.entities;

import com.github.timgoes1997.location.Location;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RegionTest {

    @Test
    public void isWithinRegionSquare() {
        RegionBorder r1 = new RegionBorder(1L, 0.0d, 0.0d);
        RegionBorder r2 = new RegionBorder(2L, 0.0d, 50.0d);
        RegionBorder r3 = new RegionBorder(3L, 50.0d, 50.0d);
        RegionBorder r4 = new RegionBorder(4L, 50.0d, 0.0d);
        List<RegionBorder> squareBorders = new ArrayList<RegionBorder>();
        squareBorders.add(r1);
        squareBorders.add(r2);
        squareBorders.add(r3);
        squareBorders.add(r4);

        Region square = new Region("square");
        square.setBorderList(squareBorders);

        Location l1 = new Location(25.0d, 25.0d);
        Location l2 = new Location(75.0d, 75.0d);
        Location l3 = new Location(0.1d, 0.1d);
        Location l4 = new Location(-0.1d, -0.1d);
        Location l5 = new Location(-0.00001d, 1.0d);

        assertTrue(square.isWithinRegion(l1));
        assertFalse(square.isWithinRegion(l2));
        assertTrue(square.isWithinRegion(l3));
        assertFalse(square.isWithinRegion(l4));
        assertFalse(square.isWithinRegion(l5));
    }

    @Test
    public void isWithinDiamondLikeShape() {
        RegionBorder r1 = new RegionBorder(1L, 0.0d, 0.0d);
        RegionBorder r2 = new RegionBorder(2L, 25.0d, 50.0d);
        RegionBorder r3 = new RegionBorder(3L, 50.0d, 0.0d);
        RegionBorder r4 = new RegionBorder(4L, 25.0d, -50.0d);
        List<RegionBorder> squareBorders = new ArrayList<RegionBorder>();
        squareBorders.add(r1);
        squareBorders.add(r2);
        squareBorders.add(r3);
        squareBorders.add(r4);

        Region square = new Region("square");
        square.setBorderList(squareBorders);

        Location l1 = new Location(25.0d, 25.0d);
        Location l2 = new Location(37.5d, 26.0d);
        Location l3 = new Location(23.d, 40.0d);
        Location l4 = new Location(24.d, -50.d);
        Location l5 = new Location(-0.00001d, 1.0d);

        assertTrue(square.isWithinRegion(l1));
        assertFalse(square.isWithinRegion(l2));
        assertTrue(square.isWithinRegion(l3));
        assertFalse(square.isWithinRegion(l4));
        assertFalse(square.isWithinRegion(l5));
    }

    @Test
    public void isInOrder() {
        RegionBorder r1 = new RegionBorder(1L, 0.0d, 0.0d);
        RegionBorder r2 = new RegionBorder(2L, 0.0d, 50.0d);
        RegionBorder r3 = new RegionBorder(3L, 50.0d, 50.0d);
        RegionBorder r4 = new RegionBorder(4L, 50.0d, 0.0d);
        List<RegionBorder> squareBorders = new ArrayList<RegionBorder>();
        squareBorders.add(r4);
        squareBorders.add(r1);
        squareBorders.add(r3);
        squareBorders.add(r2);

        Region square = new Region("square");
        square.setBorderList(squareBorders);

        List<RegionBorder> inOrder = square.getBorderList();
        assertTrue(inOrder.get(0).getRegionInsertionPointId() == 1L);
        assertTrue(inOrder.get(1).getRegionInsertionPointId() == 2L);
        assertTrue(inOrder.get(2).getRegionInsertionPointId() == 3L);
        assertTrue(inOrder.get(3).getRegionInsertionPointId() == 4L);
    }
}