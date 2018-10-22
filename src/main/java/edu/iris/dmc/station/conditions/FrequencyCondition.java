package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Sensitivity;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class FrequencyCondition extends ChannelRestrictedCondition {

	public FrequencyCondition(boolean required, String description, Restriction... restrictions) {
		super(required, description, restrictions);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("method not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		if (isRestricted(channel)) {
			return Result.success();
		}
		return evaluate(channel, channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {

		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}

		if (channel.getResponse().getInstrumentSensitivity() != null) {

			if (channel.getResponse().getInstrumentSensitivity()
					.getFrequency() > (channel.getSampleRate().getValue() / 2)) {
				return Result.warning(channel.getResponse().getInstrumentSensitivity().getFrequency() + ">"
						+ channel.getSampleRate().getValue() + "/2");
			}
		}
		return Result.success();
	}

}
