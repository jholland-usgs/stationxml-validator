package edu.iris.dmc.station.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iris.dmc.fdsn.station.model.BaseNodeType.LEVEL;

public class RuleContext {

	private Map<Integer, List<Message>> map = new HashMap<>();

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

	public List<Message> getResponse() {
		List<Message> list = new ArrayList<>();
		for (List<Message> l : map.values()) {
			list.addAll(l);
		}
		return list;
	}

	public List<Message> getResponse(int ruleId) {
		return map.get(ruleId);
	}

	public void addViolation(Message message) {
		List<Message> list = map.get(message.getRule().getId());
		if (list == null) {
			list = new ArrayList<>();
			map.put(message.getRule().getId(), list);
		}
		list.add(message);
	}

	public void clear() {
		this.map = new HashMap<>();
	}
}
