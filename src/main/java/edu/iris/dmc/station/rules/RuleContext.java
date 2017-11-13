package edu.iris.dmc.station.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iris.dmc.fdsn.station.model.BaseNodeType.LEVEL;




public class RuleContext {

	private Map<Integer, List<Result>> map = new HashMap<>();

	private LEVEL level;

	private RuleContext(LEVEL level) {
		this.level = level;
	}

	public static RuleContext of(LEVEL level) {
		return new RuleContext(level);
	}

	public LEVEL getLevel() {
		return this.level;
	}

	public List<Result> getResponse() {
		List<Result> list = new ArrayList<>();
		for (List<Result> l : map.values()) {
			list.addAll(l);
		}
		return list;
	}

	public List<Result> getResponse(int ruleId) {
		return map.get(ruleId);
	}

	public void addViolation(Result result) {
		List<Result> list = map.get(result.getRuleId());
		if (list == null) {
			list = new ArrayList<>();
			map.put(result.getRuleId(), list);
		}
		list.add(result);
	}

	public void clear() {
		this.map = new HashMap<>();
	}
}
