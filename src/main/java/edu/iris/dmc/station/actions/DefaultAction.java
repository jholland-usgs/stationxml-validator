package edu.iris.dmc.station.actions;

import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.RuleContext;

public class DefaultAction implements Action {

	public void update(RuleContext context, Result result) {
		context.addViolation(result);
	}
}
