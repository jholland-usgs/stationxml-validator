package edu.iris.dmc.station.rules;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.actions.Action;
import edu.iris.dmc.station.conditions.Condition;

public class Rule {

	private int id;
	private Condition condition;
	// private Action action;

	public Rule(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public String getDescription() {
		return this.condition.getDescription();
	}

	public void execute(Network network, RuleContext context, Action action) {
		Result result = condition.evaluate(network);
		if (!result.isSuccess()) {
			result.setRuleId(this.id);
			result.setNetwork(network);
			action.update(context, result);
		}
	}

	public void execute(Network network, Station station, RuleContext context, Action action) {
		Result result = condition.evaluate(station);
		if (!result.isSuccess()) {
			result.setRuleId(this.id);
			result.setNetwork(network);
			result.setStation(station);
			action.update(context, result);
		}
	}

	public void execute(Network network, Station station, Channel channel, RuleContext context, Action action) {
		Result result = condition.evaluate(channel);
		if (!result.isSuccess()) {
			result.setRuleId(this.id);
			result.setNetwork(network);
			result.setStation(station);
			result.setChannel(channel);
			action.update(context, result);
		}
	}

	public void execute(Network network, Station station, Channel channel, Response response, RuleContext context,
			Action action) {
		Result result = condition.evaluate(response);
		if (!result.isSuccess()) {
			result.setRuleId(this.id);
			action.update(context, result);
		}
	}

	public Condition getConditionExpression() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "Rule{" + "conditionExpression=" + condition + "'}'";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Rule rule = (Rule) o;

		if (condition != null ? !condition.equals(rule.condition) : rule.condition != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = condition != null ? condition.hashCode() : 0;
		return result;
	}
}
