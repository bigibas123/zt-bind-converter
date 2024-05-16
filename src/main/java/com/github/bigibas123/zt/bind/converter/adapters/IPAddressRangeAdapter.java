package com.github.bigibas123.zt.bind.converter.adapters;

import com.google.gson.*;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.format.IPAddressRange;

import java.lang.reflect.Type;

public class IPAddressRangeAdapter
		implements JsonSerializer<IPAddressRange>, JsonDeserializer<IPAddressRange> {
	public IPAddressRange deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new IPAddressString(json.getAsString()).getAddress();
	}

	public JsonElement serialize(IPAddressRange src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toCanonicalString());
	}

}
