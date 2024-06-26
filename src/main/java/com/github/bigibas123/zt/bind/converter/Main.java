package com.github.bigibas123.zt.bind.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Main {

	public static void main(String[] args) {
		if (!checkEnvVars()) return;
		ZerotierBindConverter converter = new ZerotierBindConverter();
		Objects.requireNonNull(converter);
		(new Thread(converter::run)).start();
	}

	private static boolean checkEnvVars() {
		Logger log = LoggerFactory.getLogger("Environment Check");
		boolean correct = true;
		if (Reference.API_TOKEN == null) {
			correct = false;
			log.info("Environment variable \"ZT_TOKEN\" not set \n" +
					"this is needed to access the api");
		}
		if (Reference.DOMAIN_PREFIX == null) {
			correct = false;
			log.info("Environment variable \"MAIN_DOMAIN\" not set \n" +
					"this is needed to know where to end all records with");
		}
		if(Reference.RESPONSIBLE_EMAIL == null){
			correct = false;
			log.info("Environment variable \"EMAIL\" not set\n" +
					"this is needed to make a proper SOA record");
		}
		if (Reference.PRIMARY_NS == null) {
			correct = false;
			log.info("Environment variable \"PRIMARY_NS\" not set \n" +
					"this is needed for the NS records and the SOA");
		}
		return correct;
	}

}