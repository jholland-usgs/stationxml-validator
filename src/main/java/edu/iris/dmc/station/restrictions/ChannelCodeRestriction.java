package edu.iris.dmc.station.restrictions;

import java.util.Arrays;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Response;

public class ChannelCodeRestriction implements Restriction {

	private String name;
	private String[] codes = new String[] { "OCF", "SOH", "ACE", "LOG" };

	public ChannelCodeRestriction() {
		this("CodeRestriction");
	}

	public ChannelCodeRestriction(String name) {
		this.name = name;
	}

	@Override
	public boolean doesQualify(Channel channel) {
		if (channel == null || channel.getCode() == null) {
			throw new IllegalArgumentException("Channel|code cannot be null");
		}
		for (String code : codes) {
			if (code.equals(channel.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doesQualify(Response response) {
		return false;
	}

	@Override
	public String toString() {
		return "ChannelCodeRestriction [name=" + name + ", codes=" + Arrays.toString(codes) + "]";
	}

}
