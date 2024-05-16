package com.github.bigibas123.zt.bind.converter.model.networklist;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Capability {
	@SerializedName("private")
	private boolean def;
	private int id;
	private List<Rule> rules;
}
