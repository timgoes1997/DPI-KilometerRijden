package com.github.timgoes1997.jms.gateway.queue;

import com.github.timgoes1997.jms.gateway.type.GatewayType;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.Properties;

public class MessageSenderGateway {

    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;
    private GatewayType gatewayType;

    public MessageSenderGateway(String channelName, String provider, GatewayType type) {
        setup(channelName, provider, type);
    }

    private void setup(String channelName, String provider, GatewayType type) {
        try {
            this.gatewayType = type;
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, provider); //"tcp://localhost:61616" <--- standaard providerURL voor localhost activemq

            // connect to the Destination called “myFirstChannel”
            // queue or topic: “queue.myFirstDestination” or “topic.myFirstDestination”
            props.put((type.toString() + channelName), channelName);

            Context jndiContext = new InitialContext(props);
            ActiveMQConnectionFactory connectionFactory = (ActiveMQConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connectionFactory.setTrustAllPackages(true);
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the sender destination
            destination = (Destination) jndiContext.lookup(channelName);
            producer = session.createProducer(destination);

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public Message createTextMessage(String body) throws JMSException {
        return session.createTextMessage(body);
    }

    public Message createObjectMessage(Serializable object) throws JMSException {
        return session.createObjectMessage(object);
    }

    public void send(Message msg) {
        try {
            producer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public GatewayType getGatewayType() {
        return gatewayType;
    }
}

