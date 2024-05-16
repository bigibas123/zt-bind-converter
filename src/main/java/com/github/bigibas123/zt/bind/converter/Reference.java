package com.github.bigibas123.zt.bind.converter;

import com.github.bigibas123.zt.bind.converter.adapters.IPAddressRangeAdapter;
import com.github.bigibas123.zt.bind.converter.adapters.IPaddressAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import inet.ipaddr.IPAddress;
import inet.ipaddr.format.IPAddressRange;

import java.util.Calendar;


public class Reference {

	public static final String API_ROOT = "https://my.zerotier.com/api";
	public static final String API_TOKEN = System.getenv("ZT_TOKEN");
	public static final String DOMAIN_PREFIX = System.getenv("MAIN_DOMAIN");
	public static final String PRIMARY_NS = System.getenv("PRIMARY_NS");
	public static final Gson gson = (new GsonBuilder())
			.registerTypeAdapter(IPAddress.class, new IPaddressAdapter())
			.registerTypeAdapter(IPAddressRange.class, new IPAddressRangeAdapter())
			.create();
	public static final boolean USE_HURRICANE = System.getenv().containsKey("USE_HURRICANE");
	private static final Calendar cal = Calendar.getInstance();
	public static final String currentDateString = "" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH) + "01";

}