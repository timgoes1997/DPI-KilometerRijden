package com.github.timgoes1997.jms.gateway.interfaces;

import com.github.timgoes1997.jms.messaging.RequestReply;

public interface DynamicServer<REQUEST, REPLY> {
    RequestReply<REQUEST, REPLY> onReceiveRequest(RequestReply<REQUEST, REPLY> request);
}
