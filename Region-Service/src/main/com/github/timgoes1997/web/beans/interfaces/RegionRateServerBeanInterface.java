package com.github.timgoes1997.web.beans.interfaces;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.request.rate.RegionRateReply;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.region.RegionReply;
import com.github.timgoes1997.request.region.RegionRequest;
import com.github.timgoes1997.request.region.RegionRequestRegionRates;

import java.util.List;

public interface RegionRateServerBeanInterface {

    RegionReply getRegionReply(RegionRequest regionRequest);
    RegionRateReply getRegionRateReply(RegionRateRequest regionRateRequest);

    RegionReply<List<RegionRate>> getRegionRatesReply(RegionRequestRegionRates regionRequestRegionRates);
    RegionReply<List<Region>> getRegionsReply();

}
