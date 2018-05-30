package com.github.timgoes1997.jms.test;

import java.io.Serializable;

public class TestReply implements Serializable {
    private String requestResponse;

    public TestReply(String requestResponse) {
        this.requestResponse = requestResponse;
    }

    public String getRequestResponse() {
        return requestResponse;
    }

    public void setRequestResponse(String requestResponse) {
        this.requestResponse = requestResponse;
    }
}
