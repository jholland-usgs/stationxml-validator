package edu.iris.dmc.station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iris.dmc.LEVEL;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.actions.Action;
import edu.iris.dmc.station.conditions.AzimuthCondition;
import edu.iris.dmc.station.conditions.CalibrationUnitCondition;
import edu.iris.dmc.station.conditions.CodeCondition;
import edu.iris.dmc.station.conditions.Condition;
import edu.iris.dmc.station.conditions.CreationTimeCondition;
import edu.iris.dmc.station.conditions.DecimationFactorCondition;
import edu.iris.dmc.station.conditions.DecimationRateCondition;
import edu.iris.dmc.station.conditions.DepthCondition;
import edu.iris.dmc.station.conditions.DipCondition;
import edu.iris.dmc.station.conditions.DistanceCondition;
import edu.iris.dmc.station.conditions.EpochOverlapCondition;
import edu.iris.dmc.station.conditions.EpochRangeCondition;
import edu.iris.dmc.station.conditions.GeoLocationCondition;
import edu.iris.dmc.station.conditions.LatitudeCondition;
import edu.iris.dmc.station.conditions.LocationCodeCondition;
import edu.iris.dmc.station.conditions.LongitudeCondition;
import edu.iris.dmc.station.conditions.MissingDecimationCondition;
import edu.iris.dmc.station.conditions.OrientationCondition;
import edu.iris.dmc.station.conditions.SampleRateCondition;
import edu.iris.dmc.station.conditions.SensorCondition;
import edu.iris.dmc.station.conditions.StageSequenceCondition;
import edu.iris.dmc.station.conditions.StageUnitCondition;
import edu.iris.dmc.station.conditions.StartTimeCondition;
import edu.iris.dmc.station.conditions.StationElevationCondition;
import edu.iris.dmc.station.rules.Rule;
import edu.iris.dmc.station.rules.RuleContext;

public class RuleEngineRegistry {

	private Map<Integer, Rule> networkRules = new HashMap<>();
	private Map<Integer, Rule> stationRules = new HashMap<>();
	private Map<Integer, Rule> channelRules = new HashMap<>();
	private Map<Integer, Rule> responseRules = new HashMap<>();

	public RuleEngineRegistry() {
		init();
	}

	private void init() {

		defaultNetworkRules();
		defaultStationRules();
		defaultChannelRules();
		defaultResponseRules();
	}

	private void defaultNetworkRules() {
		String codeRegex = "[A-Za-z0-9\\*\\?]{1,2}";
		add(101, new CodeCondition(true, "[A-Za-z0-9\\*\\?]{1,2}", codeRegex), Network.class);
		add(103, new StartTimeCondition(true, "Required field"), Network.class);

		add(105, new EpochRangeCondition(true, ""), Network.class);
		add(152, new EpochOverlapCondition(true, "Station epochs cannot overlap in time"), Network.class);
	}

	private void defaultStationRules() {
		String codeRegex = "[A-Za-z0-9\\*\\?]{1,5}";
		add(201, new CodeCondition(true, codeRegex, "Station code too long, empty string, or wrong characters"),
				Station.class);
		add(203, new StartTimeCondition(true, "Station element 'startDate' is required"), Station.class);
		add(205, new EpochRangeCondition(true,
				"Station element 'startDate' must occur before Station element 'endDate', if 'endDate' is available"),
				Station.class);

		add(206, new LatitudeCondition(true, "Invalid latitude value"), Station.class);
		add(207, new LongitudeCondition(true, "Invalid longitude value"), Station.class);

		add(209, new CreationTimeCondition(true, "Station attribute 'CreationDate' cannot be null"), Station.class);
		add(210, new StationElevationCondition(true, "Station elevation must be equal to or above Channel elevation"),
				Station.class);

		add(211, new GeoLocationCondition(true, "longitude,latitude cannot be zero"), Station.class);

		add(251, new DistanceCondition(true, "Distance from station to channel must not exceed 1 km", 1),
				Station.class);
		add(252, new EpochOverlapCondition(true, "Channel epochs cannot overlap in time"), Station.class);
	}

	private void defaultChannelRules() {
		String codeRegex = "[A-Za-z0-9\\*\\?]{1,3}";
		add(301, new CodeCondition(true, codeRegex,
				"Channel attribute 'code' cannot be null, must consist of a three-character string"), Channel.class);
		add(302, new LocationCodeCondition(true, "([A-Za-z0-9\\*\\?\\-\\ ]{1,2})?",
				"Channel attribute 'location' cannot be null, must consist of a three-character string"),
				Channel.class);
		add(303, new StartTimeCondition(true, "'startDate' is required"), Channel.class);
		add(305, new EpochRangeCondition(true, "Channel startDate must be before endDate when endDate available"),
				Channel.class);
		add(306, new LatitudeCondition(true, "Invalid latitude value"), Channel.class);
		add(307, new LongitudeCondition(true, "Invalid longitude value"), Channel.class);
		add(308, new DepthCondition(true, "depth is required"), Channel.class);

		add(309, new AzimuthCondition(true, "Invalid azimuth", 0, 90), Channel.class);
		add(310, new SampleRateCondition(false, "If Channel sample rate = 0, no Response should be included."),
				Channel.class);

		add(311, new SensorCondition(true, "Invalid azimuth"), Channel.class);
		add(312, new CalibrationUnitCondition(true, "Calibration unit is invalid"), Channel.class);

		add(313, new GeoLocationCondition(true, "longitude,latitude cannot be zero"), Channel.class);
		add(314, new DipCondition(true, "Invalid dip", -90, 90), Channel.class);
		add(315, new OrientationCondition(true, "Invalid channel orientation"), Channel.class);
	}

	private void defaultResponseRules() {
		add(401, new StageSequenceCondition(true, "Invalid channel orientation"), Response.class);
		add(402, new StageUnitCondition(true,
				"The input unit of a stage must match the output unit of the preceding stage, except for stages 0 or 1."),
				Response.class);
		add(409, new MissingDecimationCondition(true,
				"Response stages having Coefficient, FIR ResponseList or a PolesZeros with with transfer function type Digital, must include a Decimation element."),
				Response.class);

		add(413, new DecimationFactorCondition(false,
				"If the value of Stage::Decimation::Factor is > 1 then Stage::Decimation::Correction must be non-zero."),
				Response.class);
		add(414, new DecimationRateCondition(true,
				"Decimation::InputSampleRate value should be the same as the Decimation::InputSampleRate divided by the Decimation::Factor from the previous Stage."),
				Response.class);
	}

	public void add(int id, Condition condition, Class<?> clazz) {
		if (condition == null || clazz == null) {
			//
		}
		Rule ruleToAdd = new Rule(id);
		ruleToAdd.setCondition(condition);
		add(ruleToAdd, clazz);
	}

	public void add(Rule ruleToAdd, Class<?> clazz) {
		if (Network.class == clazz) {
			this.networkRules.put(ruleToAdd.getId(), ruleToAdd);
		} else if (Station.class == clazz) {
			this.stationRules.put(ruleToAdd.getId(), ruleToAdd);
		} else if (Channel.class == clazz) {
			this.channelRules.put(ruleToAdd.getId(), ruleToAdd);
		} else if (Response.class == clazz) {
			this.responseRules.put(ruleToAdd.getId(), ruleToAdd);
		} else {
			throw new IllegalArgumentException("Unsupported class definition " + clazz.getName());
		}
	}

	public Rule unregister(int id) {
		Rule rule = this.networkRules.remove(id);
		if (rule == null) {
			rule = this.stationRules.remove(id);
		}
		if (rule == null) {
			rule = this.channelRules.remove(id);
		}

		if (rule == null) {
			rule = this.responseRules.remove(id);
		}
		return rule;
	}

	public Rule getRule(int id) {
		Rule rule = this.networkRules.remove(id);
		if (rule == null) {
			rule = this.stationRules.remove(id);
		}
		if (rule == null) {
			rule = this.channelRules.remove(id);
		}

		if (rule == null) {
			rule = this.responseRules.remove(id);
		}
		return rule;
	}

	public List<Rule> getRules() {
		List<Rule> list = new ArrayList<>();

		list.addAll(this.networkRules.values());
		list.addAll(this.stationRules.values());
		list.addAll(this.channelRules.values());
		list.addAll(this.responseRules.values());
		return list;
	}

	public void executeAllRules(Network network, RuleContext context, Action action) {
		if (network != null) {
			for (Rule rule : networkRules.values()) {
				rule.execute(network, context, action);
			}
		}

		if (network.getStations() != null) {
			for (Station station : network.getStations()) {
				executeAllRules(network, station, context, action);
			}
		}
	}

	public void executeAllRules(Network network, Station station, RuleContext context, Action action) {
		if (station != null) {
			for (Rule rule : stationRules.values()) {
				rule.execute(network, station, context, action);
			}
		}
	}

	public void executeAllRules(Network network, Station station, Channel channel, RuleContext context, Action action) {
		if (channel != null) {
			for (Rule rule : channelRules.values()) {
				rule.execute(network, station, channel, context, action);

				if (context.getLevel().getValue() > LEVEL.CHANNEL.getValue()) {
					executeAllRules(network, station, channel, channel.getResponse(), context, action);
				}
			}
		}
	}

	private void executeAllRules(Network network, Station station, Channel channel, Response response,
			RuleContext context, Action action) {
		if (response != null) {
			for (Rule rule : responseRules.values()) {
				rule.execute(network, station, channel, response, context, action);
			}
		}
	}

}
