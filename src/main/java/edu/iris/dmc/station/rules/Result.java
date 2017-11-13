package edu.iris.dmc.station.rules;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;

public class Result {

	private int ruleId;
	private Network network;
	private Station station;
	private Channel channel;
	private boolean success;
	private String message;

	public Result() {
	}

	private Result(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public static Result of(boolean success, String message) {
		return new Result(success, message);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Result [ruleId=" + ruleId);

		if (network != null) {
			builder.append(", network=" + network.getCode());
		}
		if (station != null) {
			builder.append(", station=" + station.getCode());
		}
		if (channel != null) {
			builder.append(", channel=" + channel.getCode() + "|" + channel.getLocationCode());
			builder.append(channel.getStartDate() + "|" + channel.getEndDate());
		}
		builder.append(", success=" + success + ", message=" + message + "]");

		return builder.toString();
	}

}
