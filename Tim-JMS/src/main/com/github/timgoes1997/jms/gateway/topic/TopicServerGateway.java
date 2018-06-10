package com.github.timgoes1997.jms.gateway.topic;

import com.github.timgoes1997.jms.gateway.Queue.MessageSenderGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.jms.serializer.StandardMessageSerializer;

import javax.jms.JMSException;

public class TopicServerGateway<OBJECT> extends MessageSenderGateway {

    private StandardMessageSerializer<OBJECT> serializer;
    private final Class<OBJECT> objectClass;
    private String channel;

    public TopicServerGateway(String channelName, String provider, Class<OBJECT> objectClass) {
        super(channelName, provider, GatewayType.TOPIC);
        this.channel = channelName;
        this.serializer = new StandardMessageSerializer<>(objectClass);
        this.objectClass = objectClass;

    }

    public void send(StandardMessage<OBJECT> message) throws JMSException {
        super.send(super.createTextMessage(serializer.standardMessageToString(message)));
    }

    public String getChannel() {
        return channel;
    }
}
