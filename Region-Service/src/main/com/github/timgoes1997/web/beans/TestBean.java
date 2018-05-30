package com.github.timgoes1997.web.beans;

import com.github.timgoes1997.constant.Constant;
import com.github.timgoes1997.jms.gateway.object.ObjectGateway;
import com.github.timgoes1997.jms.gateway.type.GatewayType;
import com.github.timgoes1997.jms.listeners.ClientInterfaceObject;
import com.github.timgoes1997.jms.messaging.StandardMessage;
import com.github.timgoes1997.jms.test.TestObject;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Stateless
@Path("test")
public class TestBean {

    private static final String QUEUE_SEND = "QUEUE_SEND";
    private static final String QUEUE_RECEIVE = "QUEUE_RECEIVE";
    private static final String TOPIC_SEND = "TOPIC_SEND";
    private static final String TOPIC_RECEIVE = "TOPIC_RECEIVE";

    private ObjectGateway<TestObject> objectGatewayQueue;
    private ObjectGateway objectGatewayTopic;



    //@Inject
    //private Logger logger; TODO: Fix injection causes crashes for some reason so temporarily switched to sout

    @PostConstruct
    public void init() {
        try {
            objectGatewayQueue = new ObjectGateway<>(standardMessage -> System.out.println(standardMessage.toString()),
                    QUEUE_SEND,
                    QUEUE_RECEIVE,
                    Constant.DEFAULT_PROVIDER,
                    GatewayType.QUEUE,
                    TestObject.class);
            objectGatewayTopic = new ObjectGateway<>(standardMessage -> System.out.println(standardMessage.toString()),
                    QUEUE_SEND,
                    QUEUE_RECEIVE,
                    Constant.DEFAULT_PROVIDER,
                    GatewayType.TOPIC,
                    TestObject.class);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/queue/{id}")
    public StandardMessage<TestObject> onObjectGatewayQueueTest(@PathParam("id") int id) throws JMSException {
        StandardMessage<TestObject> standardMessage = new StandardMessage<>(new TestObject("Hello"));
        objectGatewayQueue.send(standardMessage);
        return standardMessage;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/topic/{id}")
    public StandardMessage<TestObject> onObjectGatewayTopicTest(@PathParam("id") int id) throws JMSException {
        StandardMessage<TestObject> standardMessage = new StandardMessage<>(new TestObject("Hello"));
        objectGatewayTopic.send(standardMessage);
        return standardMessage;
    }
}
