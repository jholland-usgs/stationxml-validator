package edu.iris.dmc.station.conditions;

import java.util.List;
import java.util.logging.Logger;

import edu.iris.dmc.fdsn.station.model.BaseFilter;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class ResponseListCondition extends ChannelRestrictedCondition {

	private static final Logger LOGGER = Logger.getLogger(ResponseListCondition.class.getName());

	public ResponseListCondition(boolean required, String description, Restriction[] restrictions) {
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
		if (channel == null) {
			return Result.success();
		}
		return evaluate(channel, channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {System.out.println("restricted");
			return Result.success();
		}
		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}
		
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			List<ResponseStage> stages = response.getStage();

			BaseFilter responseList = null;
			BaseFilter other = null;
			for (ResponseStage s : stages) {
				if (s.getResponseList() != null) {
					responseList = s.getResponseList();
				}

				if (s.getPolesZeros() != null) {
					other = s.getPolesZeros();
				}
				if (s.getCoefficients() != null) {
					other = s.getCoefficients();
				}
				if (s.getFIR() != null) {
					other = s.getFIR();
				}
			}
			
			if (responseList != null && other == null) {
				return Result.error("Response includes only ResponseList");
			}
		}

		return Result.success();
	}
}
