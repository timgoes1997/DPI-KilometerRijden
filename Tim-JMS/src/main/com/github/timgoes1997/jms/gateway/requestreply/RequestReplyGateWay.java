package com.github.timgoes1997.jms.gateway.requestreply;

import com.github.timgoes1997.jms.gateway.queue.MessageReceiverGateway;
import com.github.timgoes1997.jms.gateway.queue.MessageSenderGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;
import com.github.timgoes1997.jms.listeners.ClientInterfaceRequestReply;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.jms.serializer.RequestReplySerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class RequestReplyGateWay<REQUEST, REPLY> {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiverGateway;
    private RequestReplySerializer serializer;
    private ClientInterfaceRequestReply clientInterface;

    private final Class<REQUEST> requestClass;
    private final Class<REPLY> replyClass;

    public RequestReplyGateWay(ClientInterfaceRequestReply clientInterface, String senderChannel, String receiverChannel, String provider, Class<REQUEST> requestClass, Class<REPLY> replyClass) throws JMSException, NamingException {
        this.sender = new MessageSenderGateway(senderChannel, provider, GatewayType.QUEUE);
        this.requestClass = requestClass;
        this.replyClass = replyClass;
        this.serializer = new RequestReplySerializer<>(requestClass, replyClass);
        this.receiverGateway = new MessageReceiverGateway(receiverChannel, provider, GatewayType.QUEUE);
        this.clientInterface = clientInterface;
        this.receiverGateway.setListener(message -> {
            try {
                onReplyArrived(serializer.requestReplyFromString(((TextMessage) message).getText()));
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public void send(RequestReply rr) throws JMSException {
        sender.send(sender.createTextMessage(serializer.requestReplyToString(rr)));
    }

    public void onReplyArrived(RequestReply rr) throws JMSException {
        if(rr != null) {
            clientInterface.receivedAction(rr);
        }else{
            throw new JMSException("Received a message with a null value");
        }
    }
}
