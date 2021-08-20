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
import java.util.stream.Collectors;

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
		this.log.info("Fetched networks: " + networks.stream().map(n -> n.getConfig().getName()).collect(Collectors.joining(", ")));
		List<NetworkHandler> networkHandlers = networks.stream().map(network -> new NetworkHandler(network, Reference.DOMAIN_PREFIX, this.apiToken)).collect(Collectors.toCollection(LinkedList::new));
		this.log.info("Launching networkhandlers...");
		networkHandlers.forEach(networkHandler -> new Thread(networkHandler::go).start());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}