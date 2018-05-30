package com.github.timgoes1997.jms.listeners;

import com.github.timgoes1997.jms.messaging.RequestReply;

import javax.jms.JMSException;

public interface ClientInterfaceRequestReply {
    void receivedAction(RequestReply requestReply) throws JMSException;
}
