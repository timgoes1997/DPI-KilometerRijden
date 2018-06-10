package com.github.timgoes1997.gateway.interfaces;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.request.topic.TopicReply;

import java.math.BigDecimal;

public interface VehicleClient {
    void onReceivePriceTopic(TopicReply topicReply);
    void onReceiveInitialPrice(RegionRate regionRate);
    void onReceiveEmpty();
}
