package edu.iris.dmc.station.rules;

import java.util.ArrayList;
import java.util.List;

public class NestedMessage extends AbstractMessage {

	private List<Message> nestedMessages = new ArrayList<>();

	public NestedMessage() {
		super(null);
	}

	public void add(Message message) {
		this.nestedMessages.add(message);
	}

	public List<Message> getNestedMessages() {
		return this.nestedMessages;
	}
}
