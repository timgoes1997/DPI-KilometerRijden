package com.github.timgoes1997.jms.listeners;

import com.github.timgoes1997.jms.messaging.StandardMessage;

import javax.jms.JMSException;

public interface ClientInterfaceObject {
    void receivedAction(StandardMessage standardMessage) throws JMSException;
}
