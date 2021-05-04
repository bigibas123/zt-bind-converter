package com.github.bigibas123.zt.bind.converter;

import com.github.bigibas123.zt.bind.converter.model.networklist.Network;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ZerotierBindConverter {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
	private final String apiToken = Reference.API_TOKEN;

	public void run() {
		getNetworks();
	}

	private void getNetworks() {
		this.log.info("Starting fetch of networks...");

		Request req = Request.Get(Reference.API_ROOT + "/network").addHeader("Authorization", "bearer " + this.apiToken);

		try {
		Type list = new TypeToken<List<Network>>() {
		}.getType();
		Content cont = req.execute().returnContent();
		List<Network> networks = Reference.gson.fromJson(new InputStreamReader(cont.asStream()), list);
		StringBuilder builder = new StringBuilder("Fetched networks: ");
		networks.forEach(network -> builder.append(network.getConfig().getName()).append(", "));
		this.log.info(builder.toString());

		List<NetworkHandler> networkHandlers = new LinkedList<>();
		for (Network network: networks) {
			networkHandlers.add(new NetworkHandler(network, Reference.DOMAIN_PREFIX, this.apiToken));
		}
		this.log.info("Launching networkhandlers...");
		new Thread(() -> networkHandlers.forEach(NetworkHandler::go)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}