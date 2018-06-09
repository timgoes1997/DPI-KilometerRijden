package com.github.timgoes1997.jms.gateway.type;

public enum GatewayType {
    TOPIC {
        public String toString() {
            return "topic.";
        }
    },
    QUEUE {
        public String toString() {
            return "queue.";
        }
    }
}
