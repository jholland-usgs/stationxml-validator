package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class EpochRangeCondition extends AbstractCondition {

	public EpochRangeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		if (network == null) {
			throw new IllegalArgumentException("Network cannot be null.");
		}

		if (network.getStartDate() == null) {
			return Result.success();// is taken care of somewhere else
		}

		if (network.getStations() != null) {
			for (Station s : network.getStations()) {
				if (s.getStartDate() != null && s.getStartDate().getTime() < network.getStartDate().getTime()) {
					return Result.error("Station startDate " + XmlUtil.toText(s.getStartDate())
							+ " cannot occur before network startDate " + XmlUtil.toText(network.getStartDate()));
				}
				if (network.getEndDate() != null && s.getEndDate() != null) {
					if (s.getEndDate().getTime() > network.getEndDate().getTime()) {
						return Result.error("Station endDate " + XmlUtil.toText(s.getEndDate())
								+ " cannot occur after network endDate " + XmlUtil.toText(network.getEndDate()));
					}
				}
			}
		}
		return Result.success();
	}

	@Override
	public Message evaluate(Station station) {
		if (station == null) {
			throw new IllegalArgumentException("Station cannot be null.");
		}

		if (station.getStartDate() == null) {
			return Result.success();// is taken care of somewhere else
		}

		if (station.getChannels() != null) {
			for (Channel c : station.getChannels()) {

				if (c.getStartDate() != null && c.getStartDate().getTime() < station.getStartDate().getTime()) {
					return Result.error("Channel startDate " + XmlUtil.toText(c.getStartDate())
							+ " cannot occur before Station startDate " + XmlUtil.toText(station.getStartDate()));
				}
				if (station.getEndDate() != null && c.getEndDate() != null) {
					if (c.getEndDate().getTime() > station.getEndDate().getTime()) {
						return Result.error("Channel endDate " + XmlUtil.toText(c.getEndDate())
								+ " cannot occur after Station endDate " + XmlUtil.toText(station.getEndDate()));
					}
				}
			}
		}
		return Result.success();
	}

	@Override
	public Message evaluate(Channel channel) {
		return null;
	}

}
