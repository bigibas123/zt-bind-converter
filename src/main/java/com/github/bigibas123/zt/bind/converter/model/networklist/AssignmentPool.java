package com.github.bigibas123.zt.bind.converter.model.networklist;

import inet.ipaddr.IPAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignmentPool {

	private IPAddress ipRangeStart;
	private IPAddress ipRangeEnd;

}
