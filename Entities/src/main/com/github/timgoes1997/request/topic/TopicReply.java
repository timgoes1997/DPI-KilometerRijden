package com.github.timgoes1997.request.topic;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.location.Location;

import java.math.BigDecimal;
import java.util.Calendar;

public class TopicReply {
    private BigDecimal price; //BigDecimal
    private Calendar endTime;
    private Region region;

    public TopicReply(BigDecimal price, Region region, Calendar endTime) {
        this.price = price;
        this.region = region;
        this.endTime = endTime;
    }

    public BigDecimal getLocation() {
        return price;
    }

    public Region getRegion() {
        return region;
    }

    public Calendar getEndTime() {
        return endTime;
    }
}
