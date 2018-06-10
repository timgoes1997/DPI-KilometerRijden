package com.github.timgoes1997.web.beans.singleton;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.location.Location;
import com.github.timgoes1997.request.topic.RegionTopicReply;
import com.github.timgoes1997.request.topic.RegionTopicRequest;
import com.github.timgoes1997.util.Constant;
import com.github.timgoes1997.web.beans.RegionService;
import com.github.timgoes1997.jms.gateway.dynamic.DynamicRequestReplyServerGateway;
import com.github.timgoes1997.jms.gateway.interfaces.DynamicServer;
import com.github.timgoes1997.web.beans.endpoint.RegionEndpoint;
import com.github.timgoes1997.web.beans.endpoint.RegionInformer;
import com.github.timgoes1997.web.beans.endpoint.RegionRateInformer;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.DuplicateKeyException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@Startup
@DependsOn("RegionRateServerBean")
public class RegionRateClientBean implements DynamicServer<RegionTopicRequest, RegionTopicReply> {

    @Inject
    private Logger logger;

    @Inject
    private RegionService regionService;

    private DynamicRequestReplyServerGateway<RegionTopicRequest, RegionTopicReply> dynamicRequestReplyServerGateway;

    private List<RegionEndpoint> beanRegionEndpoints = new ArrayList<>();

    @PostConstruct
    public void init() {
        logger.info("Initialized Region Rate server bean");

        dynamicRequestReplyServerGateway = new DynamicRequestReplyServerGateway<>(this::onReceiveRequest, Constant.REGION_TOPIC_REQUEST_CHANNEL, Constant.PROVIDER, RegionTopicRequest.class, RegionTopicReply.class);

        if(beanRegionEndpoints.isEmpty()){
            List<Region> regions = regionService.getAllRegions();
            regions.forEach(this::addRegion);
        }
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
        if (inRegion == null) {
            logger.info("Received car without a region");
            return request;
        }

        RegionEndpoint regionEndpoint = RegionEndpoint.find(inRegion);
        if(regionEndpoint == null){
            logger.severe("No existing region enpoint for given region: " + inRegion.getName() + "_" + inRegion.getId());
            return request;
        }
        if(regionEndpoint.getChannelName().isEmpty()){
            logger.severe("Given regionEndpoint has no channelname: " + inRegion.getName() + "_" + inRegion.getId());
            return request;
        }


        RegionTopicReply reply = new RegionTopicReply(regionEndpoint.getChannelName(),
                regionService.getRegionRate(inRegion,
                        regionTopicRequest.getVehicleType(),
                        regionTopicRequest.getEnergyLabel()));
        RequestReply<RegionTopicRequest, RegionTopicReply> rr = request;
        rr.setReply(reply);
        return rr;
    }

    public void addRegion(Region region) {
        try {
            RegionEndpoint regionEndpoint = new RegionEndpoint(region);
            beanRegionEndpoints.add(regionEndpoint);
        } catch (DuplicateKeyException e) {
            logger.warning("Given region already exists! " + region.getName() + "_" + region.getId());
        }
    }

    public void onReceiveNewRegion(@Observes @RegionInformer Region region) {
        logger.info("Received new region: " + region.getName());
        addRegion(region);
    }

    public void onReceiveNewRegionRate(@Observes @RegionRateInformer RegionRate regionRate) {
        try {
            RegionEndpoint.sendFromRightEndpoint(regionRate);
        } catch (Exception e) {
            logger.severe("Failed to add regionrate: " + e.getMessage());
        }
    }
}
