package com.github.timgoes1997.jms.gateway.topic;

import com.github.timgoes1997.jms.gateway.Queue.MessageReceiverGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;
import com.github.timgoes1997.jms.listeners.ClientInterfaceObject;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.jms.serializer.StandardMessageSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class TopicClientGateway<OBJECT> extends MessageReceiverGateway {

    private StandardMessageSerializer serializer;
    private final Class<OBJECT> objectClass;
    private String channelName;
    private ClientInterfaceObject clientInterface;

    public TopicClientGateway(ClientInterfaceObject clientInterface, String channelName, String provider, Class<StandardMessage> objectClass) throws NamingException, JMSException {
        super(channelName, provider, GatewayType.TOPIC);
        this.serializer = new StandardMessageSerializer(objectClass);
        this.clientInterface = clientInterface;
        this.channelName = channelName;
        this.objectClass = objectClass;
        this.setListener(message -> {
            try {
                onReplyArrived(serializer.standardMessageFromString(((TextMessage) message).getText()));
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public void onReplyArrived(StandardMessage sm) throws JMSException {
        if(sm != null) {
            clientInterface.receivedAction(sm);
        }else{
            throw new JMSException("Received a message with a null value");
        }
    }

    public String getChannelName() {
        return channelName;
    }
}
