package edu.iris.dmc.station.conditions;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Result;

public class EpochRangeCondition extends AbstractCondition {

	public EpochRangeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		if (network == null || network.getStartDate() == null) {
			return Result.of(true, null);
		}

		Result result = compare(network.getStartDate(), network.getEndDate());
		if (!result.isSuccess()) {
			return result;
		}
		if (network.getStations() != null && !network.getStations().isEmpty()) {
			for (Station station : network.getStations()) {
				if (network.getStartDate().getTime() > station.getStartDate().getTime()) {
					return Result.of(false, "Network startDate " + network.getStartDate()
							+ " cannot be after station startDate " + station.getStartDate());

				}

				if (network.getEndDate() != null) {
					if (network.getEndDate().getTime() < station.getEndDate().getTime()) {
						return Result.of(false,
								"Network endDate " + network.getEndDate() + " cannot be before station endDate " + station.getEndDate());

					}
				}
			}
		}
		// return compare(station.getStartDate(), station.getEndDate());
		return Result.of(true, null);
	}

	@Override
	public Result evaluate(Station station) {
		if (station == null || station.getStartDate() == null) {
			return Result.of(true, null);
		}

		Result result = compare(station.getStartDate(), station.getEndDate());
		if (!result.isSuccess()) {
			return result;
		}
		if (station.getChannels() != null && !station.getChannels().isEmpty()) {
			for (Channel channel : station.getChannels()) {
				if (station.getStartDate().getTime() > channel.getStartDate().getTime()) {
					return Result.of(false, "Station startDate " + station.getStartDate()
							+ " cannot be after channel startDate " + channel.getStartDate());

				}

				if (station.getEndDate() != null && channel.getEndDate() != null) {
					if (station.getEndDate().before(channel.getEndDate())) {
						return Result.of(false,
								"Station endDate " + station.getEndDate() + " cannot be before channel endDate " + channel.getEndDate());

					}
				}
			}
		}
		return Result.of(true, null);
	}

	@Override
	public Result evaluate(Channel channel) {
		if (channel == null || channel.getStartDate() == null) {
			return Result.of(true, null);
		}

		return compare(channel.getStartDate(), channel.getEndDate());
	}

	private Result compare(Date start, Date end) {
		if (end == null) {
			return Result.of(true, null);
		}
		boolean bool = start.getTime() <= end.getTime();
		if (bool) {
			return Result.of(true, null);
		}
		return Result.of(false,
				"endDate " + XmlUtil.toText(end) + " cannot be before startDate " + XmlUtil.toText(start));
	}

}
