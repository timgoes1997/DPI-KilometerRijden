package com.github.timgoes1997.jms.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectSerializer<OBJECT> {
    private Gson gson; //gebruikte eerst genson maar die heeft slechte ondersteuning voor generics zoals RequestReply, terwijl gson dit met gemak ondersteund

    private final Class<OBJECT> objectClass;

    public ObjectSerializer(Class<OBJECT> objectClass){
        this.objectClass = objectClass;
        gson = new GsonBuilder().create();
    }

    public String objectToString(OBJECT object){
        return gson.toJson(object);
    }

    public OBJECT objectFromString(String str){
        return gson.fromJson(str, objectClass);
    }
}
