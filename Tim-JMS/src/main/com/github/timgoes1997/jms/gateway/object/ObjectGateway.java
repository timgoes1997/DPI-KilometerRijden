package com.github.timgoes1997.jms.gateway.object;

import com.github.timgoes1997.jms.gateway.queue.MessageReceiverGateway;
import com.github.timgoes1997.jms.gateway.queue.MessageSenderGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;
import com.github.timgoes1997.jms.listeners.ClientInterfaceObject;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.jms.serializer.ObjectSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class ObjectGateway<OBJECT> {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiverGateway;
    private ObjectSerializer<OBJECT> serializer;
    private ClientInterfaceObject clientInterface;

    private final Class<OBJECT> objectClass;

    public ObjectGateway(ClientInterfaceObject clientInterface, String senderChannel, String receiverChannel, String provider, GatewayType gatewayType, Class<OBJECT> objectClass) throws JMSException, NamingException {
        this.sender = new MessageSenderGateway(senderChannel, provider, gatewayType);
        this.objectClass = objectClass;
        this.serializer = new ObjectSerializer<>(objectClass);
        this.receiverGateway = new MessageReceiverGateway(receiverChannel, provider, gatewayType);
        this.clientInterface = clientInterface;
        this.receiverGateway.setListener(message -> {
            try {
                onReplyArrived(serializer.standardMessageFromString(((TextMessage) message).getText()));
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public void send(StandardMessage sm) throws JMSException {
        sender.send(sender.createTextMessage(serializer.standardMessageToString(sm)));
    }

    public void onReplyArrived(StandardMessage sm) throws JMSException {
        if(sm != null) {
            clientInterface.receivedAction(sm);
        }else{
            throw new JMSException("Received a message with a null value");
        }
    }
}
