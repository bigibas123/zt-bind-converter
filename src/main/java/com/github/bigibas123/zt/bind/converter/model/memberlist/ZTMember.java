package com.github.bigibas123.zt.bind.converter.model.memberlist;

import inet.ipaddr.IPAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZTMember {

	private String id;
	private String type;
	private Long clock;
	private String networkId;
	private String nodeId;
	private String controllerId;
	private Boolean hidden;
	private String name;
	private Boolean online;
	private String description;
	private MemberConfig config;
	private Long lastOnline;
	private IPAddress physicalAddress;
	private Object physicalLocation;
	private String clientVersion;
	private Integer protocolVersion;
	private Boolean supportsRulesEngine;

}
