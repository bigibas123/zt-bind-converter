package com.github.bigibas123.zt.bind.converter.model.memberlist;

import inet.ipaddr.IPAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberConfig {

	private Boolean activeBridge;
	private String address;
	private Boolean authorized;
	private List<Integer> capabilities;
	private Long creationTime;
	private String id;
	private String identity;
	private List<IPAddress> ipAssignments;
	private Long lastAuthorizedTime;
	private Long lastDeauthorizedTime;
	private Boolean noAutoAssignIps;
	private String nwid;
	private String objtype;
	private Integer remoteTraceLevel;
	private String remoteTraceTarget;
	private Integer revision;
	private List<List<Integer>> tags;
	private Integer vMajor;
	private Integer vMinor;
	private Integer vRev;
	private Integer vProto;

}
