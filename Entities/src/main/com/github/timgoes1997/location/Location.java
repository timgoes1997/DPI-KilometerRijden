package com.github.timgoes1997.location;

import java.io.Serializable;

public class Location implements Serializable{
    private double x, y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
