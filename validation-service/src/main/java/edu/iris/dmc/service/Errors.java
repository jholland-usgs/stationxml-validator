package edu.iris.dmc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.iris.dmc.validation.rule.Severity;

public class Errors {

	private List<Integer> ignoreList = new ArrayList<Integer>();
	private List<Error> list = new ArrayList<Error>();

	public Errors() {
	}

	public Errors(List<Integer> ignoreList) {
		if (ignoreList != null && !ignoreList.isEmpty()) {
			this.ignoreList = ignoreList;
		}
	}

	public void add(Error error) {
		if (ignoreList.contains(error.getId())) {
			return;
		}
		this.list.add(error);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public void add(Severity severity, String network, Date nStart, Date nEnd, String station, Date sStart, Date sEnd,
			String location, String channel, Date cStart, Date cEnd, String path, Object invalidValue, String message) {
		if (message == null || message.trim().isEmpty()) {
			throw new IllegalArgumentException("Message cannot be null");
		}

		this.list.add(new Error(severity, network, nStart, nEnd, station, sStart, sEnd, location, channel, cStart, cEnd,
				path, invalidValue, message));

	}

	public List<Error> getAll() {
		return this.list;
	}
}
