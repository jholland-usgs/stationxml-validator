package edu.iris.dmc.station.actions;

import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.RuleContext;

public class DefaultAction implements Action {

	public void update(RuleContext context, Message message) {
		context.addViolation(message);
	}
}
