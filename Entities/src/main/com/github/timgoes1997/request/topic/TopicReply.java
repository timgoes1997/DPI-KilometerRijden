package com.github.timgoes1997.request.topic;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.location.Location;

public class TopicReply {
    private Location location;
    private Region region;

    public TopicReply(Location location, Region region) {
        this.location = location;
        this.region = region;
    }

    public Location getLocation() {
        return location;
    }

    public Region getRegion() {
        return region;
    }
}
