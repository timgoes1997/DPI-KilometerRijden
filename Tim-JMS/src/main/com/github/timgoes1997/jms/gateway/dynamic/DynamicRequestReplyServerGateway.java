package com.github.timgoes1997.jms.gateway.dynamic;

import com.github.timgoes1997.jms.gateway.Queue.MessageReceiverGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;
import com.github.timgoes1997.jms.messaging.RequestReply;
import com.github.timgoes1997.jms.serializer.RequestReplySerializer;
import com.github.timgoes1997.jms.gateway.interfaces.DynamicServer;

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
    private String provider;

    public DynamicRequestReplyServerGateway(DynamicServer<REQUEST, REPLY> requestReplyDynamicServerListener, String requestChannel, String provider, Class<REQUEST> requestClass, Class<REPLY> replyClass) {
        this.requestClass = requestClass;
        this.replyClass = replyClass;
        this.provider = provider;
        this.serializer = new RequestReplySerializer<>(requestClass, replyClass);
        this.requestReplyDynamicServerListener = requestReplyDynamicServerListener;
        try {
            this.messageReceiverGateway = new MessageReceiverGateway(requestChannel, provider, GatewayType.QUEUE);
            this.messageReceiverGateway.setListener(message -> {
                try {
                    onReceivedRequest(serializer.requestReplyFromString(((TextMessage) message).getText()));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public void onReceivedRequest(RequestReply rr) {
        if (rr != null) {
            synchronized (this) { //This block needs to be synchronized or multiple clients would cause issues, unless you start caching all the messagesenders but that could cause other issues.
                RequestReply requestReply = requestReplyDynamicServerListener.onReceiveRequest(rr);

                if (channelName.isEmpty()) {
                    LOGGER.severe("No filled in channel name, so can't send");
                    return;
                }

                try {
                    if (messageSenderGateway != null && messageSenderGateway.getChannelName().equals(channelName)) {
                        messageSenderGateway.send(messageSenderGateway.createTextMessage(serializer.requestReplyToString(requestReply)));
                        return;
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }

                try {
                    //if (messageSenderGateway != null) messageSenderGateway.close();
                    messageSenderGateway = new DynamicMessageSenderGateway(channelName, provider, GatewayType.QUEUE);
                    messageSenderGateway.send(messageSenderGateway.createTextMessage(serializer.requestReplyToString(requestReply)));
                } catch (JMSException e) {
                    LOGGER.severe("Failed at sending dynamic message: " + e.getErrorCode());
                }
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
            if (messageSenderGateway != null) messageSenderGateway.close();
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
