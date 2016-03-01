package edu.iris.dmc.validation.validator;

import edu.iris.dmc.validation.rule.Seed;

public abstract class AbstractSeedValidator {
	protected Seed seed;

	protected void initialize(Seed seed) {
		this.seed = seed;
	}
}
