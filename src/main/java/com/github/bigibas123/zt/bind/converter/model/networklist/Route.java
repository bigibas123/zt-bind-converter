package com.github.bigibas123.zt.bind.converter.model.networklist;

import inet.ipaddr.IPAddress;
import inet.ipaddr.format.IPAddressRange;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Route {
	private IPAddressRange target;
	private IPAddress via;
}
