package com.github.bigibas123.zt.bind.converter.model.networklist;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class Tag {
	private int id;
	@SerializedName("default")
	private int def;
	private Map<String, Integer> enums;
	private Map<String, Integer> flags;
}
