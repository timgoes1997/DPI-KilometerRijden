package com.github.timgoes1997.jms.serializer;

import com.github.timgoes1997.jms.messaging.RequestReply;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class RequestReplySerializer<REQUEST, REPLY> {
    private Gson gson; //gebruikte eerst genson maar die heeft slechte ondersteuning voor generics zoals RequestReply, terwijl gson dit met gemak ondersteund

    private final Class<REQUEST> requestClass;
    private final Class<REPLY> replyClass;

    public RequestReplySerializer(Class<REQUEST> requestClass, Class<REPLY> replyClass){
        this.requestClass = requestClass;
        this.replyClass = replyClass;
        gson = new GsonBuilder().create();
    }

    public String requestToString(REQUEST request){
        return gson.toJson(request);
    }

    public REQUEST requestFromString(String str){
        return gson.fromJson(str, requestClass);
    }

    public RequestReply requestReplyFromString(String str){
        return gson.fromJson(str, TypeToken.getParameterized(RequestReply.class, requestClass, replyClass).getType());
    }

    public String requestReplyToString(RequestReply rr){
        return gson.toJson(rr);
    }

    public String replyToString(REPLY reply){
        return gson.toJson(reply);
    }

    public REPLY replyFromString(String str){
        return gson.fromJson(str, replyClass);
    }
}