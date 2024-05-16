package com.github.bigibas123.zt.bind.converter.model.networklist;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class NetworkConfig {
	private String authTokens;
	private long creationTime;
	private List<Capability> capabilities;
	private boolean enableBroadcast;
	private String id;
	private List<AssignmentPool> ipAssignmentPools;
	private long lastModified;
	private int mtu;
	private int multicastLimit;
	private String name;
	@SerializedName("private")
	private boolean isPrivate;
	private int remoteTraceLevel;
	private String remoteTraceTarget;
	private List<Route> routes;
	private List<Rule> rules;
	private List<Tag> tags;
	private Map<String, Boolean> v4AssignMode;
	private Map<String, Boolean> v6AssignMode;
}
