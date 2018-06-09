package com.github.timgoes1997.jms.gateway.Queue;

import com.github.timgoes1997.jms.gateway.type.GatewayType;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class MessageReceiverGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private GatewayType gatewayType;

    public MessageReceiverGateway(String channelName, String provider, GatewayType type) throws NamingException, JMSException {
        setup(channelName, provider, type);
    }

    private void setup(String channelName, String provider, GatewayType type) throws NamingException, JMSException {
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

        // connect to the receiver destination
        destination = (Destination) jndiContext.lookup(channelName);
        consumer = session.createConsumer(destination);

        connection.start(); // this is needed to start receiving messages
    }

    public void setListener(MessageListener ml) throws JMSException {
        consumer.setMessageListener(ml);
    }

    public GatewayType getGatewayType() {
        return gatewayType;
    }
    public void close() throws JMSException {
        connection.close();
    }
}
