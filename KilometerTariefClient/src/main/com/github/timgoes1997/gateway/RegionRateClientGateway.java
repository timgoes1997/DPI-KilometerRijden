package com.github.timgoes1997.gateway;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.gateway.interfaces.RegionRateClient;
import com.github.timgoes1997.gateway.interfaces.RegionRateClientListener;
import com.github.timgoes1997.jms.gateway.requestreply.RequestReplyGateWay;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.jms.serializer.ObjectSerializer;
import com.github.timgoes1997.request.RegionReplyType;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequestType;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;
import com.github.timgoes1997.request.region.RegionRequestType;
import com.github.timgoes1997.util.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class RegionRateClientGateway implements RegionRateClient {

    private static final Logger LOGGER = Logger.getLogger(RegionRateClientGateway.class.getName());

    private RequestReplyGateWay<RegionRequest, RegionReply> regionRRG;
    private RequestReplyGateWay<RegionRateRequest, RegionRateReply> regionRateRRG;
    private RegionRateClientListener listener;
    private Gson gson;
    private String uniqueId;

    public RegionRateClientGateway(RegionRateClientListener listener) throws NamingException, JMSException {
        this.uniqueId = UUID.randomUUID().toString();
        this.listener = listener;
        this.regionRRG =
                new RequestReplyGateWay<>(this::handleRegionRequestReply, Constant.REGION_REQUEST_CHANNEL,
                        Constant.REGION_REPLY_CHANNEL, Constant.PROVIDER, RegionRequest.class, RegionReply.class);
        this.regionRateRRG =
                new RequestReplyGateWay<>(this::handleRegionRateRequestReply, Constant.REGION_RATE_REQUEST_CHANNEL,
                        Constant.REGION_RATE_REPLY_CHANNEL, Constant.PROVIDER, RegionRateRequest.class, RegionRateReply.class);
        gson = new Gson(); //Need custom serializer for ArrayList<Region>
    }

    public RegionRateClientGateway(RegionRateClientListener listener, String provider) throws NamingException, JMSException {
        this.uniqueId = UUID.randomUUID().toString();
        this.listener = listener;
        this.regionRRG =
                new RequestReplyGateWay<>(this::handleRegionRequestReply, Constant.REGION_REQUEST_CHANNEL,
                        Constant.REGION_REPLY_CHANNEL, provider, RegionRequest.class, RegionReply.class);
        this.regionRateRRG =
                new RequestReplyGateWay<>(this::handleRegionRateRequestReply, Constant.REGION_REQUEST_CHANNEL,
                        Constant.REGION_REPLY_CHANNEL, provider, RegionRateRequest.class, RegionRateReply.class);
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
        RegionRequest regionRequest = new RegionRequest(region, RegionRequestType.GET_RATES);
        RequestReply<RegionRequest, RegionReply> rr = new RequestReply<>(uniqueId, regionRequest, null);
        regionRRG.send(rr);
    }

    @Override
    public void handleRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr) {
        if (rr.getReply() == null || rr.getRequest() == null) {
            LOGGER.severe("Received empty reply or request");
            return;
        }
        if (!rr.getId().equals(uniqueId)) {
            LOGGER.info("Received message that was not for me");
            return;
        }

        try {
            switch (rr.getRequest().getRegionRequestType()) {
                case GET_ALL:
                    checkReplyGetAll(rr.getReply());
                    break;
                case GET_RATES:
                    checkReplyGetRates(rr.getReply());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkReplyGetAll(RegionReply regionReply) {
        if (regionReply.getObject() == null) {
            LOGGER.info("Received empty region reply");
            return;
        }

        try {
            String json = gson.toJson(regionReply.getObject());
            List<Region> regions = gson.fromJson(json, new TypeToken<ArrayList<Region>>() {
            }.getType());
            listener.onReceiveRegions(regions);
        } catch (Exception e) {
            LOGGER.severe("Failed to get List region from given json object");
        }
    }

    @Override
    public void checkReplyGetRates(RegionReply regionReply) {
        if (regionReply.getObject() == null) {
            LOGGER.info("Received empty region reply");
            return;
        }

        try {
            String json = gson.toJson(regionReply.getObject());
            List<RegionRate> regionRates = gson.fromJson(json, new TypeToken<ArrayList<RegionRate>>() {
            }.getType());
            listener.onReceiveRegionRates(regionRates);
        } catch (Exception e) {
            LOGGER.severe("Failed to get List region from given json object");
        }
    }

    @Override
    public void handleRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr) {
        if (rr.getReply() == null || rr.getRequest() == null) {
            LOGGER.severe("Received empty reply or request");
        }
        if (rr.getReply().getStatus() == RegionReplyType.CANCELED) {
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
