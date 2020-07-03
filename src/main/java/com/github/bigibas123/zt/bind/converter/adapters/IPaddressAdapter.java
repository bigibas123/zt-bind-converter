package com.github.bigibas123.zt.bind.converter.adapters;

import com.google.gson.*;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;

import java.lang.reflect.Type;


public class IPaddressAdapter
    implements JsonSerializer<IPAddress>, JsonDeserializer<IPAddress> {

    public IPAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (new IPAddressString(json.getAsString())).getAddress();
    }


    public JsonElement serialize(IPAddress src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toFullString());
    }

}
