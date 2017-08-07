package edu.iris.dmc.station;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.actions.Action;
import edu.iris.dmc.station.conditions.Condition;
import edu.iris.dmc.station.rules.Rule;
import edu.iris.dmc.station.rules.RuleContext;

public class RuleEngineService {

	private RuleEngineRegistry ruleEngineRegistry;

	public RuleEngineService() {
		this.ruleEngineRegistry = new RuleEngineRegistry();
	}

	public void setRuleEngineRegistry(RuleEngineRegistry ruleEngineRegistry) {
		this.ruleEngineRegistry = ruleEngineRegistry;
	}

	public void registerRule(int id, Condition condition, Class clazz) {
		this.ruleEngineRegistry.add(id, condition, clazz);
	}

	public void unregister(int id) {
		this.ruleEngineRegistry.unregister(id);
	}

	public void executeAllRules(FDSNStationXML document, RuleContext context, Action action) {
		if (context == null) {
			throw new IllegalArgumentException("Rules Context cannot be null");
		}
		if (document != null) {
			if (document.getNetwork() != null) {
				for (Network network : document.getNetwork()) {
					executeAllRules(network, context, action);
				}
			}
		}
	}

	public void executeAllRules(Network network, RuleContext context, Action action) {
		if (network != null) {
			this.ruleEngineRegistry.executeAllRules(network, context, action);
		}
	}

	public void executeAllRules(Network network, Station station, RuleContext context, Action action) {
		if (station != null) {
			this.ruleEngineRegistry.executeAllRules(network, station, context, action);
		}
	}

	public void executeAllRules(Network network, Station station, Channel channel, RuleContext context, Action action) {
		if (channel != null) {
			this.ruleEngineRegistry.executeAllRules(network, station, channel, context, action);
		}
	}

	public List<Rule> getRules() {
		return this.ruleEngineRegistry.getRules();
	}

	public Rule getRule(int id) {
		return this.ruleEngineRegistry.getRule(id);
	}

}
