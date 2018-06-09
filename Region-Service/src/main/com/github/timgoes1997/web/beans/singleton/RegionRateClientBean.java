package com.github.timgoes1997.web.beans.singleton;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.location.Location;
import com.github.timgoes1997.request.topic.RegionTopicReply;
import com.github.timgoes1997.request.topic.RegionTopicRequest;
import com.github.timgoes1997.util.Constant;
import com.github.timgoes1997.web.beans.RegionService;
import com.github.timgoes1997.jms.gateway.dynamic.DynamicRequestReplyServerGateway;
import com.github.timgoes1997.jms.gateway.interfaces.DynamicServer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;

@Singleton
@Startup
public class RegionRateClientBean implements DynamicServer<RegionTopicRequest, RegionTopicReply> {

    @Inject
    private Logger logger;

    @Inject
    private RegionService regionService;

    private DynamicRequestReplyServerGateway<RegionTopicRequest, RegionTopicReply> dynamicRequestReplyServerGateway;

    @PostConstruct
    public void init() {
        logger.info("Initialized Region Rate server bean");

        dynamicRequestReplyServerGateway = new DynamicRequestReplyServerGateway<>(this::onReceiveRequest, Constant.REGION_TOPIC_REQUEST_CHANNEL, Constant.PROVIDER, RegionTopicRequest.class, RegionTopicReply.class);
    }

    @Override
    public RequestReply<RegionTopicRequest, RegionTopicReply> onReceiveRequest(RequestReply<RegionTopicRequest, RegionTopicReply> request) {
        RegionTopicRequest regionTopicRequest = request.getRequest();
        if (regionTopicRequest == null) {
            logger.severe("Received null RegionTopicRequest");
            return request;
        }
        dynamicRequestReplyServerGateway.setChannelName(regionTopicRequest.getClientChannel());
        Location location = regionTopicRequest.getLocation();
        Region inRegion = regionService.getWithinRegion(location.getX(), location.getY());
        RegionTopicReply reply = new RegionTopicReply("test"); //TODO: connection with topic
        /*
            TODO: Kijk naar hoe ik websockets heb aangemaakt om zo synchroon nieuwe topicbeans aan te maken en statische op te slaan + te verwijderen.
         */
        return null;
    }
}
