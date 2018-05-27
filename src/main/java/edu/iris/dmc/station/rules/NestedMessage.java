package edu.iris.dmc.station.rules;

import java.util.ArrayList;
import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;

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

	@Override
	public void setRule(Rule rule) {
		super.setRule(rule);
		for(Message message:nestedMessages){
			message.setRule(rule);
		}
	}

	@Override
	public void setNetwork(Network network) {
		super.setNetwork(network);
		for(Message message:nestedMessages){
			message.setNetwork(network);
		}
	}

	@Override
	public void setStation(Station station) {
		super.setStation(station);
		for(Message message:nestedMessages){
			message.setStation(station);
		}
	}

	@Override
	public void setChannel(Channel channel) {
		super.setChannel(channel);
		for(Message message:nestedMessages){
			message.setChannel(channel);
		}
	}
	
}
