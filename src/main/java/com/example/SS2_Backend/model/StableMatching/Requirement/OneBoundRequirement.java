package com.example.SS2_Backend.model.StableMatching.Requirement;

import lombok.Getter;

import static com.example.SS2_Backend.util.Utils.formatDouble;

@Getter
public class OneBoundRequirement extends Requirement {
	@Getter
	private final double bound;
	private final String expression;

	public OneBoundRequirement(double bound, String expression) {
		super(1);
		this.bound = bound;
		this.expression = expression;
	}

	public String toString() {
		return "[" + formatDouble(bound) + ", " + expression + "]";
	}
}
