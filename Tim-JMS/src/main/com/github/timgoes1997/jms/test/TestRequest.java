package com.github.timgoes1997.jms.test;

import java.io.Serializable;

public class TestRequest implements Serializable {
    private int requestID;

    public TestRequest(int requestID) {
        this.requestID = requestID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }
}
