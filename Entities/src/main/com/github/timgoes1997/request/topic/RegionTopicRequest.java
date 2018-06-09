package com.github.timgoes1997.request.topic;

import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.location.Location;

public class RegionTopicRequest {

    private Location location;
    private VehicleType vehicleType;
    private EnergyLabel energyLabel;
    private String clientChannel;

    public RegionTopicRequest(Location location, VehicleType vehicleType, EnergyLabel energyLabel, String clientChannel) {
        this.location = location;
        this.clientChannel = clientChannel;
        this.vehicleType = vehicleType;
        this.energyLabel = energyLabel;
    }

    public Location getLocation() {
        return location;
    }

    public String getClientChannel() {
        return clientChannel;
    }
}
