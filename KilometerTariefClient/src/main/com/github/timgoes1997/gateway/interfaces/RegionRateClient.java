package com.github.timgoes1997.gateway.interfaces;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.rate.RegionRateRequestType;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;

import javax.jms.JMSException;

public interface RegionRateClient {
    void create(RegionRate rate) throws JMSException;
    void update(RegionRate rate) throws JMSException;
    void delete(RegionRate rate) throws JMSException;
    void request(RegionRate rate, RegionRateRequestType type) throws JMSException;

    void getRegions() throws JMSException;
    void getRegionRates(Region region) throws JMSException;
    void checkReplyGetAll(RegionReply regionReply);
    void checkReplyGetRates(RegionReply regionReply);
    void handleRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr);
    void handleRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr);
}
