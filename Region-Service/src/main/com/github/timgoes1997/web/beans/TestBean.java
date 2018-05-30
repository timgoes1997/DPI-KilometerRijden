package com.github.timgoes1997.web.beans;

import com.github.timgoes1997.jms.gateway.requestreply.RequestReplyGateWay;

import javax.ejb.Stateless;

@Stateless
public class TestBean {


    private RequestReplyGateWay requestReplyGateWay;
}
