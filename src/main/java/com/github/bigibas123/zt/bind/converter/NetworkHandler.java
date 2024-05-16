package com.github.bigibas123.zt.bind.converter;

import com.github.bigibas123.zt.bind.converter.model.memberlist.ZTMember;
import com.github.bigibas123.zt.bind.converter.model.networklist.Network;
import com.github.bigibas123.zt.bind.converter.util.Util;
import com.google.gson.reflect.TypeToken;
import inet.ipaddr.IPAddress;
import inet.ipaddr.format.IPAddressRange;
import lombok.SneakyThrows;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;


public class NetworkHandler {

	private final Logger log;
	private final String apiToken;
	private final Network network;
	private final String prefix;
	private List<ZTMember> members;
	private File netDir;

	public NetworkHandler(Network network, String prefix, String token) {
		this.network = network;
		this.log = LoggerFactory.getLogger(getClass().getSimpleName() + "-" + network.getConfig().getName());
		this.apiToken = token;
		this.prefix = prefix;
	}

	@SneakyThrows
	public void go() {
		this.log.info("Getting info for: " + this.network.getConfig().getName());
		this.log.trace("InfoGet: " + this.network);
		removeOldDir();
		makeNewDir();


		Request req = Request.Get(Reference.API_ROOT + "/network/" + this.network.getId() + "/member").addHeader("Authorization", "bearer " + this.apiToken);
		Content cont = req.execute().returnContent();

		Type list = new TypeToken<List<ZTMember>>() {
		}.getType();

		this.members = Reference.gson.fromJson(new InputStreamReader(cont.asStream()), list);

		StringBuilder builder = new StringBuilder("Fetched members: ");
		this.members.forEach(member -> builder.append(member.getName()).append(", "));
		this.log.info(builder.toString());
		(new Thread(this::writeNormalZone)).start();
		(new Thread(this::writeReverseZone)).start();
	}

	@SneakyThrows
	private void removeOldDir() {
		File dir = new File(this.network.getConfig().getName());
		if (dir.exists())
			if (dir.isDirectory()) {
				this.log.info("Removing old directory for: " + dir.getName());

				try (var fileStream = Files.walk(dir.toPath())) {
					fileStream.sorted(Comparator.reverseOrder())
							.map(Path::toFile)
							.forEach(file -> {

								if (file.delete()) {
									this.log.debug("Deleted: " + file.getPath());
								} else {
									this.log.error("Failed deleting: " + file.getPath());
								}
							});
				}

			} else if (dir.delete()) {
				this.log.debug("Deleted: " + dir.getPath());
			} else {
				this.log.warn("Could not delete: " + dir.getAbsolutePath() + " this is not severe but could lead to other errors");
			}
	}

	@SneakyThrows
	private void makeNewDir() {
		this.netDir = new File(this.network.getConfig().getName());
		if (!this.netDir.mkdirs())
			throw new FileSystemException("Could not make directory: " + this.netDir.getAbsolutePath());
	}

	private void writeNormalZone() {
		log.info("Start writing normal zone");
		File file = new File(this.netDir, this.prefix + ".zone");
		try {
			if (!file.createNewFile())
				throw new IOException("Failed creating regular zone file");

			try {
				FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);

				try {
					writer.append("$ORIGIN ").append(this.prefix).append(".\n");
					writer.append("$TTL 3600\n");

					writeSOA(writer);

					writer.append(Util.formatRootNS(Reference.PRIMARY_NS + "."));
					if (Reference.USE_HURRICANE) {
						writer.append(Util.getHurricaneNS());
					}

					for (ZTMember member : this.members) {
						String memberdomain = Util.dnsFormatMember(member);
						for (IPAddress ip : member.getConfig().getIpAssignments()) {
							writeARecord(writer, memberdomain, ip);
						}
					}

					writer.close();
				} catch (Throwable throwable) {
					try {
						writer.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					}
					throw throwable;
				}
			} catch (IOException e) {
				this.log.error("IOException when writing to regular zone file:", e);
			}

		} catch (IOException e) {
			this.log.error("IOException when creating regular zone file:", e);
		}
		log.info("Done writing normal zone");
	}


	@SneakyThrows
	private void writeReverseZone() {
		log.info("Starting reverse zone writing");
		for (IPAddressRange ipAddressRange : Util.getLocalSubnets(this.network)) {
			String reverseDomain;


			if (ipAddressRange.getLower().isIPv6()) {
				reverseDomain = Util.toReverseIPv6Domain(ipAddressRange.getLower().toIPv6().getNetworkSection().withoutPrefixLength());
			} else if (ipAddressRange.getLower().isIPv4()) {
				reverseDomain = Util.toReverseIPv4Domain(ipAddressRange.getLower().toIPv4().getNetworkSection().withoutPrefixLength());
			} else {
				continue;
			}

			File file = new File(this.netDir, reverseDomain + "zone");

			new Thread(() -> writeReverseAdressRange(ipAddressRange, file, reverseDomain)).start();
		}
	}

	private void writeSOA(Appendable writer) throws IOException {
		writeSOA(writer, this.prefix + ".");
	}

	private void writeARecord(Appendable writer, String name, IPAddress ip) throws IOException {
		if (ip.isIPv4() || ip.isIPv6()) {
			if (ip.isIPv4()) {
				writer.append(Util.formatRecord(name, "A", ip.toNormalizedString()));
			} else if (ip.isIPv6()) {
				writer.append(Util.formatRecord(name, "AAAA", ip.toNormalizedString()));
			}
		} else {
			this.log.warn("Unrecognized ip type: " + ip.toNormalizedString());
		}
	}

	private void writeReverseAdressRange(IPAddressRange range, File file, String reverseDomain) {
		log.info("Writing reverse for: " + range);
		try {
			if (!file.createNewFile())
				throw new IOException("Failed creating reverse zone file");

			try {
				FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);

				try {
					writer.append("$ORIGIN ").append(reverseDomain).append("\n");
					writer.append("$TTL 3600\n");

					writeSOA(writer, reverseDomain);

					writer.append(Util.formatRootNS(Reference.PRIMARY_NS + "."));
					if (Reference.USE_HURRICANE) {
						writer.append(Util.getHurricaneNS());
					}


					for (ZTMember member : this.members) {
						String memberdomain = Util.dnsFormatMember(member) + "." + prefix + ".";
						for (IPAddress ip : member.getConfig().getIpAssignments()) {
							if (!range.contains(ip)) continue;
							writer.append(Util.formatReverseRecord(ip, memberdomain));
						}
					}


					writer.close();
				} catch (Throwable throwable) {
					try {
						writer.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					}
					throw throwable;
				}
			} catch (IOException e) {
				this.log.error("IOException when writing to reverse zone file:", e);
			}

		} catch (IOException e) {
			this.log.error("IOException when creating reverse zone file:", e);
		}
		log.info("Done writing reverse for: " + range);
	}

	private void writeSOA(Appendable writer, String domain) throws IOException {
		writer.append(domain).append(" IN SOA ").append(Reference.PRIMARY_NS).append(". root.").append(Reference.PRIMARY_NS).append(". (\n")
				.append("\t").append(Reference.currentDateString).append(" ;Serial\n")
				.append("\t3600\t;Refresh\n")
				.append("\t1800\t;Retry\n")
				.append("\t604800\t;Expire\n")
				.append("\t1800\t;Minimum TTL\n")
				.append(")\n")
		;
	}

}