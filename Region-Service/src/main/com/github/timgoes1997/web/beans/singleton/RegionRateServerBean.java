package com.github.timgoes1997.web.beans.singleton;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;
import com.github.timgoes1997.request.region.RegionRequestRegionRates;
import com.github.timgoes1997.web.beans.RegionService;
import com.github.timgoes1997.web.beans.interfaces.RegionRateServerBeanInterface;
import com.github.timgoes1997.web.gateway.RegionRateServerGateway;
import com.github.timgoes1997.web.gateway.interfaces.RegionRateServerListener;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@Startup
public class RegionRateServerBean implements RegionRateServerBeanInterface {

    private RegionRateServerGateway serverGateway;

    @Inject
    private RegionService regionService;

    @Inject
    private Logger logger;

    @PostConstruct
    public void init(){
        logger.info("Initialized Region Rate server bean");
        try {
            serverGateway = new RegionRateServerGateway(new RegionRateServerListener() {
                @Override
                public void onReceiveRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr) {
                    RequestReply<RegionRequest, RegionReply> response = rr;
                    response.setReply(getRegionReply(rr.getRequest()));
                    try {
                        serverGateway.sendRegionRequestReply(response);
                    } catch (JMSException e) {
                        logger.severe(e.getErrorCode());
                    }
                }

                @Override
                public void onReceiveRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr) {
                    RequestReply<RegionRateRequest, RegionRateReply> response = rr;
                    response.setReply(getRegionRateReply(rr.getRequest()));
                    try {
                        serverGateway.sendRegionRateRequestReply(response);
                    } catch (JMSException e) {
                        logger.severe(e.getErrorCode());
                    }
                }

                @Override
                public void onError(String info, Exception e) {
                    logger.severe(info + ":" + System.lineSeparator() + e.getMessage());
                }
            });
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RegionReply getRegionReply(RegionRequest regionRequest) {
        if(regionRequest.getRegionRequestType() == null){
            return new RegionReply();
        }
        switch (regionRequest.getRegionRequestType()){
            case GET_RATES:
                if(regionRequest instanceof RegionRequestRegionRates){
                    return getRegionRatesReply((RegionRequestRegionRates) regionRequest);
                }else{
                    break;
                }
            case GET_ALL:
                return getRegionsReply();
        }
        return new RegionReply();
    }

    @Override
    public RegionRateReply getRegionRateReply(RegionRateRequest regionRateRequest) {
        return null;
    }

    @Override
    public RegionReply<List<RegionRate>> getRegionRatesReply(RegionRequestRegionRates regionRequestRegionRates) {
        if(regionRequestRegionRates.getRegion() == null || !regionService.regionExists(regionRequestRegionRates.getRegion())){
            return new RegionReply<>();
        }

        return new RegionReply<>(
                regionService.getRegionRates(regionRequestRegionRates.getRegion()),
                regionRequestRegionRates.getRegion());
    }

    @Override
    public RegionReply<List<Region>> getRegionsReply() {
        return new RegionReply<>(regionService.getAllRegions());
    }
}
