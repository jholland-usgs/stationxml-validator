package edu.iris.dmc.station.conditions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.Success;

public class EpochRangeCondition extends AbstractCondition {

	public EpochRangeCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		if (network == null || network.getStartDate() == null) {
			return Result.success();
		}

		Message result = compare(network.getStartDate(), network.getEndDate());
		if (!(result instanceof Success)) {
			return result;
		}
		if (network.getStations() != null && !network.getStations().isEmpty()) {
			for (Station station : network.getStations()) {
				if (network.getStartDate().getTime() > station.getStartDate().getTime()) {
					return Result.error( "Network startDate " + network.getStartDate()
							+ " cannot be after station startDate " + station.getStartDate());

				}

				if (network.getEndDate() != null) {
					if (network.getEndDate().getTime() < station.getEndDate().getTime()) {
						return Result.error( "Network endDate " + network.getEndDate()
								+ " cannot be before station endDate " + station.getEndDate());

					}
				}
			}
		}
		// return compare(station.getStartDate(), station.getEndDate());
		return Result.success();
	}

	@Override
	public Message evaluate(Station station) {
		if (station == null || station.getStartDate() == null) {
			return Result.success();
		}

		Message result = compare(station.getStartDate(), station.getEndDate());
		if (!(result instanceof Success)) {
			return result;
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (station.getChannels() != null && !station.getChannels().isEmpty()) {
			for (Channel channel : station.getChannels()) {
				if (station.getStartDate().getTime() > channel.getStartDate().getTime()) {
					return Result.error("Station " + station.getCode() + " startDate "
							+ dateFormatter.format(station.getStartDate()) + " cannot be after channel "
							+ channel.getCode() + ":" + channel.getLocationCode() + " startDate "
							+ dateFormatter.format(channel.getStartDate()));

				}

				if (station.getEndDate() != null && channel.getEndDate() != null) {
					if (station.getEndDate().before(channel.getEndDate())) {
						return Result.error("Station endDate " + dateFormatter.format(station.getEndDate())
								+ " cannot be before channel endDate " + dateFormatter.format(channel.getEndDate()));

					}
				}
			}
		}
		return Result.success();
	}

	@Override
	public Message evaluate(Channel channel) {
		if (channel == null || channel.getStartDate() == null) {
			return Result.success();
		}

		return compare(channel.getStartDate(), channel.getEndDate());
	}

	private Message compare(Date start, Date end) {
		if (end == null) {
			return Result.success();
		}
		boolean bool = start.getTime() <= end.getTime();
		if (bool) {
			return Result.success();
		}
		return Result.error("endDate " + XmlUtil.toText(end) + " cannot be before startDate " + XmlUtil.toText(start));
	}

}
