package com.github.timgoes1997.web.gateway.interfaces;

import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;

import javax.jms.JMSException;

public interface RegionRateServer {
    void handleRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr);
    void handleRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr);

    void sendRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr) throws JMSException;
    void sendRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr) throws JMSException;
}
