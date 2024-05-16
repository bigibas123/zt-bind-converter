package com.github.bigibas123.zt.bind.converter.util;

import com.github.bigibas123.zt.bind.converter.model.memberlist.ZTMember;
import com.github.bigibas123.zt.bind.converter.model.networklist.Network;
import inet.ipaddr.IPAddress;
import inet.ipaddr.format.IPAddressRange;
import inet.ipaddr.ipv4.IPv4Address;
import inet.ipaddr.ipv4.IPv4AddressSection;
import inet.ipaddr.ipv4.IPv4AddressSegment;
import inet.ipaddr.ipv6.IPv6Address;
import inet.ipaddr.ipv6.IPv6AddressSection;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@UtilityClass
public class Util {

	private final Logger log = LoggerFactory.getLogger(Util.class);

	public String formatReverseRecord(IPAddress address, String domain) {
		if (address.isIPv4()) {
			return formatIPv4ReverseRecord(address.toIPv4(), domain);
		} else if (address.isIPv6()) {
			return formatIPv6ReverseRecord(address.toIPv6(), domain);
		} else {
			log.error("Unrecognized ip: " + address);
			return "";
		}
	}

	public String formatIPv4ReverseRecord(IPv4Address address, String domain) {
		String reverseAddress = toReverseIPv4Domain(address);
		return formatRecord(reverseAddress, "PTR", domain);
	}

	public String formatIPv6ReverseRecord(IPv6Address address, String domain) {
		String reverseAddress = toReverseIPv6Domain(address);
		return formatRecord(reverseAddress, "PTR", domain);
	}

	public String toReverseIPv4Domain(IPv4Address address) {
		return toReverseIPv4Domain(address.getNetworkSection());
	}

	public String formatRecord(String name, String recordType, String value) {
		return String.format("%s\t%s\t%s\n", name, recordType, value);
	}

	public String toReverseIPv6Domain(IPv6Address address) {
		return toReverseIPv6Domain(address.getSection());
	}

	public String toReverseIPv4Domain(IPv4AddressSection address) {
		StringBuilder reverseAddress = new StringBuilder();
		List<IPv4AddressSegment> octets = Arrays.asList(address.getSegments());
		Collections.reverse(octets);
		for (IPv4AddressSegment octet : octets) {
			reverseAddress.append(octet.toString()).append(".");
		}
		reverseAddress.append("in-addr.arpa.");
		return reverseAddress.toString();
	}

	public String toReverseIPv6Domain(IPv6AddressSection address) {
		StringBuilder builder = new StringBuilder();
		byte[] octets = address.getBytes();

		for (Byte b : octets) {
			String[] nibbles = String.format("%02X", b).split("");

			for (String nibble : nibbles) {
				builder.append(nibble).append(".");
			}
		}

		builder.reverse().deleteCharAt(0).append(".ip6.arpa.");
		return builder.toString();
	}

	public List<IPAddressRange> getLocalSubnets(Network network) {
		List<IPAddressRange> list = new LinkedList<>();
		network.getConfig().getRoutes().stream().filter((route) -> route.getVia() == null)
				.forEach((route) -> list.add(route.getTarget()));
		log.debug("Found local routes for network " + network.getConfig().getName() + ": " + list);
		return list;
	}

	public String dnsFormatMember(ZTMember member) {
		return member.getName().replace("-", ".").toLowerCase();
	}

	public static String formatRootNS(String nsServer) {
		return formatRecord("@", "NS", nsServer);
	}

	public static String getHurricaneNS() {
		StringBuilder builder = new StringBuilder();
		for (int value = 1; value <= 5; value++) {
			builder.append(Util.formatRootNS("ns" + value + ".he.net."));
		}
		return builder.toString();
	}
}