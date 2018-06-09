package com.github.timgoes1997.gateway.interfaces;

import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.location.Location;
import com.github.timgoes1997.request.topic.RegionTopicReply;
import com.github.timgoes1997.request.topic.RegionTopicRequest;
import com.github.timgoes1997.request.topic.TopicReply;

import javax.jms.JMSException;

public interface VehicleClientGatewayInterface {
    void sendRegionRequest(Location location, VehicleType vehicleType, EnergyLabel energyLabel) throws JMSException;

    void handleRegionRequestReply(RequestReply<RegionTopicRequest, RegionTopicReply> rr);
    void handleTopicReply(StandardMessage<TopicReply> sm);
}
