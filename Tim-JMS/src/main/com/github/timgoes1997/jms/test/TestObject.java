package com.github.timgoes1997.jms.test;

import java.io.Serializable;

public class TestObject implements Serializable {

    private String test;

    public TestObject(String test) {
        this.test = test;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
