package edu.iris.dmc.station.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleContext {

	private Map<Integer, List<Message>> map = new HashMap<>();

	private boolean ignoreWarnings = false;
	private List<Integer> ignoreRules = new ArrayList<Integer>();

	private RuleContext(boolean ignoreWarnings) {
		this.ignoreWarnings = ignoreWarnings;
	}

	public static RuleContext of(boolean includeWarnings) {
		return new RuleContext(includeWarnings);
	}

	public boolean isIgnoreWarnings() {
		return this.ignoreWarnings;
	}

	public void ignoreRule(String text) {
		if (text == null) {
			return;
		}
		String[] array = text.split(",");

		for (String s : array) {
			if (!s.trim().isEmpty()) {
				ignoreRule(Integer.valueOf(s));
			}
		}
	}

	public void ignoreRules(int[] nums) {
		for (int n : nums) {
			this.ignoreRule(n);
		}
	}

	public void ignoreRule(int num) {
		this.ignoreRules.add(num);
	}

	public List<Message> list() {
		List<Message> list = new ArrayList<>();
		for (List<Message> l : map.values()) {
			list.addAll(l);
		}
		return list;
	}

	public Map<Integer, List<Message>> map() {
		return this.map;
	}

	public List<Message> getMessages(int ruleId) {
		return map.get(ruleId);
	}

	public void addViolation(Message message) {
		if (message instanceof Warning && this.ignoreWarnings) {
			return;
		}
		if (message instanceof NestedMessage) {
			for (Message m : ((NestedMessage) message).getNestedMessages()) {
				m.setSource(message.getSource());
				addViolation(m);
			}
			return;
		}
		List<Message> list = map.get(message.getRule().getId());
		if (list == null) {
			list = new ArrayList<>();
			map.put(message.getRule().getId(), list);
		}
		list.add(message);
	}

	public List<Integer> getIgnoreRules() {
		return ignoreRules;
	}

	public void clear() {
		this.map = new HashMap<>();
	}
}
