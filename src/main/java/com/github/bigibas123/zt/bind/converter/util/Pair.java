package com.github.bigibas123.zt.bind.converter.util;

import lombok.Data;

@Data
public final class Pair<T1, T2> {

	private final T1 x;
	private final T2 y;

}