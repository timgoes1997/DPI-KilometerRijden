package com.github.timgoes1997.web.gateway;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.jms.gateway.requestreply.RequestReplyGateWay;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;
import com.github.timgoes1997.util.Constant;
import com.github.timgoes1997.web.gateway.interfaces.RegionRateServer;
import com.github.timgoes1997.web.gateway.interfaces.RegionRateServerListener;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.List;
import java.util.logging.Logger;

public class RegionRateServerGateway implements RegionRateServer {
    private static final Logger LOGGER = Logger.getLogger(RegionRateServerGateway.class.getName());

    private RequestReplyGateWay<RegionRequest, RegionReply> serverRegionRRG;
    private RequestReplyGateWay<RegionRateRequest, RegionRateReply> serverRegionRateRRG;
    private RegionRateServerListener listener;

    public RegionRateServerGateway(RegionRateServerListener listener) throws NamingException, JMSException {
        this.listener = listener;
        this.serverRegionRRG =
                new RequestReplyGateWay<>(this::handleRegionRequestReply, Constant.REGION_REPLY_CHANNEL,
                        Constant.REGION_REQUEST_CHANNEL, Constant.PROVIDER, RegionRequest.class, RegionReply.class);
        this.serverRegionRateRRG =
                new RequestReplyGateWay<>(this::handleRegionRateRequestReply, Constant.REGION_RATE_REPLY_CHANNEL,
                        Constant.REGION_RATE_REQUEST_CHANNEL, Constant.PROVIDER, RegionRateRequest.class, RegionRateReply.class);
    }

    /**
     * Checks for empty requests, if not they are send to the listener otherwise they are canceled and send back.
     * @param rr the request.
     */
    @Override
    public void handleRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr) {
        if (rr.getRequest() == null) {
            LOGGER.severe("Received empty request");
            RequestReply<RegionRequest, RegionReply> failed = rr;
            failed.setReply(new RegionReply());
            try {
                sendRegionRequestReply(failed);
            } catch (JMSException e) {
                listener.onError("Received empty Region request but couldn't send response", e);
            }
        }
        listener.onReceiveRegionRequestReply(rr);
    }

    /**
     * Checks for empty requests, if not they are send to the listener otherwise they are canceled and send back.
     * @param rr the request.
     */
    @Override
    public void handleRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr) {
        if (rr.getRequest() == null) {
            LOGGER.severe("Received empty request");
            RequestReply<RegionRateRequest, RegionRateReply> failed = rr;
            failed.setReply(new RegionRateReply());
            try {
                sendRegionRateRequestReply(failed);
            }catch (Exception e){
                listener.onError("Received empty RegionRate request but couldn't send response", e);
            }
        }
        listener.onReceiveRegionRateRequestReply(rr);
    }

    @Override
    public void sendRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr) throws JMSException {
        serverRegionRRG.send(rr);
    }

    @Override
    public void sendRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr) throws JMSException {
        serverRegionRateRRG.send(rr);
    }
}