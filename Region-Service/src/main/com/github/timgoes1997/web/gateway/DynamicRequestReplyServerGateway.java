package com.github.timgoes1997.web.gateway;

import com.github.timgoes1997.jms.gateway.Queue.MessageReceiverGateway;
import com.github.timgoes1997.jms.gateway.Queue.MessageSenderGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.jms.serializer.RequestReplySerializer;
import com.github.timgoes1997.util.Constant;
import com.github.timgoes1997.web.gateway.interfaces.DynamicServer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import java.util.logging.Logger;

public class DynamicRequestReplyServerGateway<REQUEST, REPLY> {

    private static final Logger LOGGER = Logger.getLogger(DynamicRequestReplyServerGateway.class.getName());

    private MessageReceiverGateway messageReceiverGateway;
    private DynamicMessageSenderGateway messageSenderGateway;
    private DynamicServer<REQUEST, REPLY> requestReplyDynamicServerListener;
    private RequestReplySerializer serializer;

    private final Class<REQUEST> requestClass;
    private final Class<REPLY> replyClass;

    private String channelName;

    public DynamicRequestReplyServerGateway(DynamicServer<REQUEST, REPLY> requestReplyDynamicServerListener, String requestChannel, String provider, Class<REQUEST> requestClass, Class<REPLY> replyClass) {
        this.requestClass = requestClass;
        this.replyClass = replyClass;
        this.serializer = new RequestReplySerializer<>(requestClass, replyClass);
        this.requestReplyDynamicServerListener = requestReplyDynamicServerListener;
        try {
            this.messageReceiverGateway = new MessageReceiverGateway(requestChannel, provider, GatewayType.QUEUE);
            this.messageReceiverGateway.setListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        onReceivedRequest(serializer.requestReplyFromString(((TextMessage) message).getText()));
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public void onReceivedRequest(RequestReply rr) {
        if (rr != null) {
            RequestReply requestReply = requestReplyDynamicServerListener.onReceiveRequest(rr);

            if (channelName.isEmpty()) {
                LOGGER.severe("No filled in channel name, so can't send");
                return;
            }

            if(messageSenderGateway.getChannelName().equals(channelName)){
                try {
                    messageSenderGateway.send(messageSenderGateway.createTextMessage(serializer.requestReplyToString(rr)));
                    return;
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

            try {
                messageSenderGateway = new DynamicMessageSenderGateway(channelName, Constant.PROVIDER, GatewayType.QUEUE);
                messageSenderGateway.send(messageSenderGateway.createTextMessage(serializer.requestReplyToString(rr)));
            } catch (JMSException e) {
                LOGGER.severe("Failed at sending dynamic message: " + e.getErrorCode());
            }
        } else {
            try {
                throw new JMSException("Received a message with a null value");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            messageReceiverGateway.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
