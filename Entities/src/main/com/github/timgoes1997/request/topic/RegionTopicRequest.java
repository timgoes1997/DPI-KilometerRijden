package com.github.timgoes1997.request.topic;

import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.location.Location;

public class RegionTopicRequest {

    private Location location;
    private VehicleType vehicleType;
    private String clientChannel;

    public RegionTopicRequest(Location location, VehicleType vehicleType, String clientChannel) {
        this.location = location;
        this.clientChannel = clientChannel;
        this.vehicleType = vehicleType;
    }

    public Location getLocation() {
        return location;
    }

    public String getClientChannel() {
        return clientChannel;
    }
}
