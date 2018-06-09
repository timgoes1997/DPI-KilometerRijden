package com.github.timgoes1997.jms.gateway.dynamic;

import com.github.timgoes1997.jms.gateway.Queue.MessageSenderGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;

public class DynamicMessageSenderGateway extends MessageSenderGateway {

    private String channelName;

    public DynamicMessageSenderGateway(String channelName, String provider, GatewayType type) {
        super(channelName, provider, type);
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
}
