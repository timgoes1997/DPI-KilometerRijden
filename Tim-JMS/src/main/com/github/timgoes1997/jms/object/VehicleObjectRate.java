package com.github.timgoes1997.jms.object;

public class VehicleObjectRate {
    private long timestamp;
    private int rate;
    private int distance;

    public VehicleObjectRate(long timestamp, int rate, int distance){
        this.timestamp = timestamp;
        this.rate = rate;
        this.distance = distance;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "VehicleObjectRate{" +
                "timestamp=" + timestamp +
                ", rate=" + rate +
                ", distance=" + distance +
                '}';
    }
}
