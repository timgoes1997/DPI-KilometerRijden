package com.github.timgoes1997.request.topic;

import com.github.timgoes1997.entities.RegionRate;

public class RegionTopicReply {

    private String topicChannel;
    private RegionRate regionRate;

    public RegionTopicReply(String topicChannel, RegionRate regionRate) {
        this.topicChannel = topicChannel;
        this.regionRate = regionRate;
    }

    public String getTopicChannel() {
        return topicChannel;
    }

    public RegionRate getRegionRate() {
        return regionRate;
    }
}
