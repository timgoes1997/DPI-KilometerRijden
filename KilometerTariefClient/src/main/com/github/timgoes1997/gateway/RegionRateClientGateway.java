package com.github.timgoes1997.gateway;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.gateway.interfaces.RegionRateClient;
import com.github.timgoes1997.gateway.interfaces.RegionRateClientListener;
import com.github.timgoes1997.jms.gateway.requestreply.RequestReplyGateWay;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.request.rate.RegionRateReplyType;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequestType;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;
import com.github.timgoes1997.request.region.RegionRequestRegionRates;
import com.github.timgoes1997.request.region.RegionRequestType;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class RegionRateClientGateway implements RegionRateClient {

    private static final Logger LOGGER = Logger.getLogger(RegionRateClientGateway.class.getName());

    private RequestReplyGateWay<RegionRequest, RegionReply> regionRRG;
    private RequestReplyGateWay<RegionRateRequest, RegionRateReply> regionRateRRG;
    private RegionRateClientListener listener;
    private String uniqueId;

    public RegionRateClientGateway(RegionRateClientListener listener) throws NamingException, JMSException {
        this.uniqueId = UUID.randomUUID().toString();
        this.listener = listener;
        this.regionRRG =
                new RequestReplyGateWay<>(this::handleRegionRequestReply, "RegionRateClientRequest",
                        "RegionRateServerResponse", "tcp://localhost:61616",
                        RegionRequest.class, RegionReply.class);
    }

    public RegionRateClientGateway(RegionRateClientListener listener, String provider) throws NamingException, JMSException {
        this.uniqueId = UUID.randomUUID().toString();
        this.listener = listener;
        this.regionRRG =
                new RequestReplyGateWay<>(this::handleRegionRequestReply, "RegionRateClientRequest",
                        "RegionRateServerResponse", provider,
                        RegionRequest.class, RegionReply.class);
    }

    @Override
    public void create(RegionRate rate) throws JMSException {
        request(rate, RegionRateRequestType.CREATE);
    }

    @Override
    public void update(RegionRate rate) throws JMSException {
        request(rate, RegionRateRequestType.UPDATE);
    }

    @Override
    public void delete(RegionRate rate) throws JMSException {
        request(rate, RegionRateRequestType.DELETE);
    }

    @Override
    public void request(RegionRate rate, RegionRateRequestType type) throws JMSException {
        RegionRateRequest rrr = new RegionRateRequest(rate, type);
        RequestReply<RegionRateRequest, RegionRateReply> rr = new RequestReply<>(uniqueId, rrr, null);
        regionRateRRG.send(rr);
    }

    @Override
    public void getRegions() throws JMSException {
        RegionRequest regionRequest = new RegionRequest(RegionRequestType.GET_ALL);
        RequestReply<RegionRequest, RegionReply> rr = new RequestReply<>(uniqueId, regionRequest, null);
        regionRRG.send(rr);
    }

    @Override
    public void getRegionRates(Region region) throws JMSException {
        RegionRequestRegionRates regionRequest = new RegionRequestRegionRates(RegionRequestType.GET_RATES, region);
        RequestReply<RegionRequest, RegionReply> rr = new RequestReply<>(uniqueId, regionRequest, null);
        regionRRG.send(rr);
    }

    @Override
    public void handleRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr) {
        if (rr.getReply() == null || rr.getRequest() == null) {
            LOGGER.severe("Received empty reply or request");
        }
        if (!rr.getId().equals(uniqueId)) return;

        try {
            switch (rr.getRequest().getRegionRequestType()) {
                case GET_ALL:
                    if ((rr.getReply().getObject() instanceof List) && ((List) rr.getReply().getObject()).get(0) instanceof Region) {
                        listener.onReceiveRegions((List<Region>) rr.getReply().getObject());
                    }
                    break;
                case GET_RATES:
                    if ((rr.getReply().getObject() instanceof List) && ((List) rr.getReply().getObject()).get(0) instanceof RegionRate) {
                        listener.onReceiveRegionRates((List<RegionRate>) rr.getReply().getObject());
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr) {
        if (rr.getReply() == null || rr.getRequest() == null) {
            LOGGER.severe("Received empty reply or request");
        }
        if (rr.getReply().getStatus() == RegionRateReplyType.CANCELED) {
            if (rr.getId().equals(uniqueId)) {
                listener.onClientRequestCanceled(rr.getRequest());
            }
            return;
        }

        switch (rr.getRequest().getRegionRateRequestType()) {
            case CREATE:
                listener.onReceiveRegionRateCreate(rr.getReply().getRegionRate());
                break;
            case DELETE:
                listener.onReceiveRegionRateRemove(rr.getReply().getRegionRate());
                break;
            case UPDATE:
                listener.onReceiveRegionRateUpdate(rr.getReply().getRegionRate());
                break;
        }

    }

}
