package com.github.timgoes1997.web.beans.endpoint;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.jms.gateway.topic.TopicServerGateway;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.request.topic.TopicReply;
import com.github.timgoes1997.util.Constant;

import javax.ejb.DuplicateKeyException;
import javax.jms.JMSException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

public class RegionEndpoint {
    private static final Logger LOGGER = Logger.getLogger(RegionEndpoint.class.getName());

    private static Set<RegionEndpoint> regionEndpoints = new CopyOnWriteArraySet<>();

    private TopicServerGateway<TopicReply> regionGateWay;
    private Region region;
    private String channelName;

    public RegionEndpoint(Region region) throws DuplicateKeyException {
        if(region == null){
            throw new NullPointerException("Region can't be null while creating this object");
        }
        if(find(region) != null){
            throw new DuplicateKeyException("Already exists");
        }
        this.region = region;
        this.channelName = Constant.REGION_TOPIC_SERVER + region.getName() + "_" + region.getId();
        this.regionGateWay = new TopicServerGateway<>(channelName, Constant.PROVIDER, TopicReply.class);
        regionEndpoints.add(this);
    }

    public void send(RegionRate regionRate){
        try {
            StandardMessage<TopicReply> message = new StandardMessage<>(
                    new TopicReply(regionRate.getKilometerPrice(), region, regionRate.getEndTime()));
            regionGateWay.send(message);
        } catch (JMSException e) {
            LOGGER.severe("Failed to send standardmessage topicreply for: " + region.getName() + "_"
                    + region.getId() + "rate" + regionRate.getId());
            e.printStackTrace();
        }
    }

    public static RegionEndpoint find(Region region){
        synchronized (regionEndpoints){
            Optional<RegionEndpoint> endpoint = regionEndpoints
                    .stream()
                    .filter(regionEndpoint -> regionEndpoint.getRegion().getId().equals(region.getId()))
                    .findFirst();
            return endpoint.orElse(null);
        }
    }

    public static void sendFromRightEndpoint(RegionRate regionRate){
        if(regionRate == null || regionRate.getRegion() == null){
            throw new NullPointerException("Given object is null");
        }

        RegionEndpoint regionEndpoint = find(regionRate.getRegion());
        if(regionEndpoint == null){
            throw new NullPointerException("There is no endpoint for given region");
        }

        regionEndpoint.send(regionRate);
    }

    public Region getRegion() {
        return region;
    }

    public String getChannelName() {
        return channelName;
    }
}
