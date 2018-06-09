package com.github.timgoes1997.web.beans.singleton;

import com.github.timgoes1997.web.gateway.RegionRateServerGateway;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Logger;

@Singleton
@Startup
public class RegionRateServerBean {

    private RegionRateServerGateway serverGateway;

    @Inject
    private Logger logger;

    @PostConstruct
    public void init(){
        logger.info("Initialized Region Rate server bean");
    }

}
