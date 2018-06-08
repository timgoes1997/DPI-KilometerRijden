package com.github.timgoes1997.jms.gateway.object;

import com.github.timgoes1997.jms.gateway.Queue.MessageReceiverGateway;
import com.github.timgoes1997.jms.gateway.Queue.MessageSenderGateway;
import com.github.timgoes1997.jms.listeners.ClientInterfaceObject;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.jms.serializer.StandardMessageSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class ObjectGateway<OBJECT> {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;
    private StandardMessageSerializer serializer;
    private ClientInterfaceObject clientInterface;

    private final Class<OBJECT> objectClass;

    public ObjectGateway(ClientInterfaceObject clientInterface, String senderChannel, String receiverChannel, String provider, Class<OBJECT> objectClass) throws JMSException, NamingException {
        this.sender = new MessageSenderGateway(senderChannel, provider);
        this.objectClass = objectClass;
        this.serializer = new StandardMessageSerializer(objectClass);
        this.receiver = new MessageReceiverGateway(receiverChannel, provider);
        this.clientInterface = clientInterface;
        this.receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    onReplyArrived(serializer.standardMessageFromString(((TextMessage) message).getText()));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
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

    public void close() {
        try {
            sender.close();
            receiver.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
