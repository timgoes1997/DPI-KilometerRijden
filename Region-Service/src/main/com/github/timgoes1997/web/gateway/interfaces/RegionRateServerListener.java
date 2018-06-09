package com.github.timgoes1997.web.gateway.interfaces;

import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;

public interface RegionRateServerListener {
    void onReceiveRegionRequestReply(RequestReply<RegionRequest, RegionReply> rr);
    void onReceiveRegionRateRequestReply(RequestReply<RegionRateRequest, RegionRateReply> rr);

    void onError(String info, Exception e);
}
