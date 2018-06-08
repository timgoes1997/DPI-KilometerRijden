package com.github.timgoes1997.jms.messaging;

import java.io.Serializable;
import java.util.UUID;

/**
 * RequestReply class
 *
 * @param <REQUEST> Request you want to make.
 * @param <REPLY> Reply you want to get.
 */
public class RequestReply<REQUEST,REPLY> implements Serializable {

    private String id;
    private REQUEST request;
    private REPLY reply;

    public RequestReply(String id, REQUEST request,  REPLY reply) {
        setRequest(request);
        setReply(reply);
        this.id = id;
    }

    public RequestReply(REQUEST request,  REPLY reply) {
        setRequest(request);
        setReply(reply);
    }

    public REQUEST getRequest() {
        return request;
    }

    private void setRequest(REQUEST request) {
        this.request = request;
    }

    public REPLY getReply() {
        return reply;
    }

    public void setReply(REPLY reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return request.toString() + "  --->  " + ((reply!=null)?reply.toString():"waiting for reply...");
    }

    public String getId() {
        return id;
    }
}