package com.github.timgoes1997.gateway;

import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.gateway.interfaces.VehicleClient;
import com.github.timgoes1997.gateway.interfaces.VehicleClientGatewayInterface;
import com.github.timgoes1997.jms.gateway.Queue.MessageReceiverGateway;
import com.github.timgoes1997.jms.gateway.requestreply.RequestReplyGateWay;
import com.github.timgoes1997.jms.gateway.topic.TopicClientGateway;
import com.github.timgoes1997.jms.listeners.ClientInterfaceRequestReply;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.location.Location;
import com.github.timgoes1997.request.topic.RegionTopicReply;
import com.github.timgoes1997.request.topic.RegionTopicRequest;
import com.github.timgoes1997.request.topic.TopicReply;
import com.github.timgoes1997.util.Constant;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.UUID;
import java.util.logging.Logger;

public class VehicleClientGateway implements VehicleClientGatewayInterface {

    private static final Logger LOGGER = Logger.getLogger(VehicleClientGateway.class.getName());

    private VehicleClient vehicleClient;
    private RequestReplyGateWay<RegionTopicRequest, RegionTopicReply> topicRequestGateway;
    private TopicClientGateway<TopicReply> topic;
    private String clientChannel;
    private String uniqueId;

    public VehicleClientGateway(VehicleClient vehicleClient) throws NamingException, JMSException {
        this.vehicleClient = vehicleClient;
        this.uniqueId = UUID.randomUUID().toString();
        this.clientChannel = Constant.REGION_TOPIC_CLIENT + uniqueId;
        this.topicRequestGateway =
                new RequestReplyGateWay<>(this::handleRegionRequestReply, Constant.REGION_TOPIC_REQUEST_CHANNEL,
                        clientChannel, Constant.PROVIDER,  RegionTopicRequest.class, RegionTopicReply.class);
    }

    @Override
    public void sendRegionRequest(Location location, VehicleType vehicleType, EnergyLabel energyLabel) throws JMSException {
        RequestReply<RegionTopicRequest, RegionTopicReply> rr = new RequestReply<>(
                new RegionTopicRequest(location, vehicleType, energyLabel, clientChannel), null);
        topicRequestGateway.send(rr);
    }

    @Override
    public void handleRegionRequestReply(RequestReply<RegionTopicRequest, RegionTopicReply> rr) {
        if(rr.getReply() == null || rr.getReply().getTopicChannel().isEmpty()){
            LOGGER.info("Retrieved invalid topicChannel");
        }

        String topicChannel = rr.getReply().getTopicChannel();
        if(topic != null){
            if(topic.getChannelName().equals(topicChannel)) return;
            if(topic.getChannelName().equals(topicChannel)) return;
            try {
                topic.close();
            } catch (JMSException e) {
                LOGGER.severe("Failed to close topic");
                return;
            }
        }

        try {
            topic = new TopicClientGateway<>(this::handleTopicReply, topicChannel, Constant.PROVIDER, TopicReply.class);
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
        //verkrijg gedoe dan maak topichannel aan.
    }

    @Override
    public void handleTopicReply(StandardMessage<TopicReply> sm) {
        if(sm.getObject() == null){
            LOGGER.severe("Received no TopicReply");
            return;
        }
        vehicleClient.onReceivePrice(sm.getObject());
    }
}
