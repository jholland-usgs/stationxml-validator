package edu.iris.dmc.station;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.conditions.AzimuthCondition;
import edu.iris.dmc.station.conditions.CalibrationUnitCondition;
import edu.iris.dmc.station.conditions.CodeCondition;
import edu.iris.dmc.station.conditions.Condition;
import edu.iris.dmc.station.conditions.DecimationCondition;
import edu.iris.dmc.station.conditions.DecimationSampleRateCondition;
import edu.iris.dmc.station.conditions.DigitalFilterCondition;
import edu.iris.dmc.station.conditions.DipCondition;
import edu.iris.dmc.station.conditions.DistanceCondition;
import edu.iris.dmc.station.conditions.EmptySensitivityCondition;
import edu.iris.dmc.station.conditions.EpochOverlapCondition;
import edu.iris.dmc.station.conditions.EpochRangeCondition;
import edu.iris.dmc.station.conditions.FrequencyCondition;
import edu.iris.dmc.station.conditions.LatitudeCondition;
import edu.iris.dmc.station.conditions.LocationCodeCondition;
import edu.iris.dmc.station.conditions.LongitudeCondition;
import edu.iris.dmc.station.conditions.MissingDecimationCondition;
import edu.iris.dmc.station.conditions.OrientationCondition;
import edu.iris.dmc.station.conditions.PolesZerosCondition;
import edu.iris.dmc.station.conditions.PolynomialCondition;
import edu.iris.dmc.station.conditions.ResponseListCondition;
import edu.iris.dmc.station.conditions.SampleRateCondition;
import edu.iris.dmc.station.conditions.SensorCondition;
import edu.iris.dmc.station.conditions.StageGainNonZeroCondition;
import edu.iris.dmc.station.conditions.StageGainProductCondition;
import edu.iris.dmc.station.conditions.StageSequenceCondition;
import edu.iris.dmc.station.conditions.StageUnitCondition;
import edu.iris.dmc.station.conditions.StartTimeCondition;
import edu.iris.dmc.station.conditions.StationElevationCondition;
import edu.iris.dmc.station.conditions.UnitCondition;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.ResponsePolynomialRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Rule;

public class RuleEngineRegistry {

	private Map<Integer, Rule> networkRules = new HashMap<>();
	private Map<Integer, Rule> stationRules = new HashMap<>();
	private Map<Integer, Rule> channelRules = new HashMap<>();
	private Map<Integer, Rule> responseRules = new HashMap<>();

	public RuleEngineRegistry(List<Integer> ignoreRules) {
		init(ignoreRules);
	}

	private void init(List<Integer> ignoreRules) {

		defaultNetworkRules(ignoreRules);
		defaultStationRules(ignoreRules);
		defaultChannelRules(ignoreRules);
		defaultResponseRules(ignoreRules);

		if (ignoreRules != null && !ignoreRules.isEmpty()) {
			for (Integer i : ignoreRules) {
				networkRules.remove(i);
				stationRules.remove(i);
				channelRules.remove(i);
				responseRules.remove(i);
			}
		}
	}

	private void defaultNetworkRules(List<Integer> ignoreRules) {
		String codeRegex = "[A-Z0-9_\\*\\?]{1,2}";
		add(101, new CodeCondition(true, codeRegex,
				"Network:Code must be assigned a string consisting of 1-2 uppercase characters A-Z and or numeric characters 0-9."),
				Network.class);
		add(110, new StartTimeCondition(true,
				"Network:startDate must occur before Network:endDate if Network:endDate is available."), Network.class);
		add(111, new EpochOverlapCondition(true,
				"Station:Epoch cannot be partly concurrent with any other Station:Epoch encompassed in parent Network:Epoch."),
				Network.class);

		add(112, new EpochRangeCondition(true,
				"Network:Epoch must encompass all subordinate Station:Epoch [Epoch=startDate-endDate]"), Network.class);
	}

	private void defaultStationRules(List<Integer> ignoreRules) {
		String codeRegex = "[A-Z0-9_\\*\\?]{1,5}";
		add(201, new CodeCondition(true, codeRegex,
				"Station:Code must be assigned a string consisting of 1-5 uppercase characters A-Z and or numeric characters 0-9."),
				Station.class);
		add(210, new StartTimeCondition(true,
				"Station:startDate is required and must occur before Station:endDate if Station:endDate is available."),
				Station.class);
		add(211, new EpochOverlapCondition(true,
				"Channel:Epoch cannot be partly concurrent with any other Channel:Epoch encompassed in parent Station:Epoch."),
				Station.class);

		add(212, new EpochRangeCondition(true,
				"Station:Epoch must encompass all subordinate Channel:Epoch [Epoch=startDate-endDate]"), Station.class);

		add(220, new LatitudeCondition(true,
				"Station:Latitude must be assigned a value that is not 0 and between -90 and 90."), Station.class);
		add(221, new LongitudeCondition(true,
				"Station:Longitude must be assigned a value that is not 0 and between -180 and 180."), Station.class);
		add(222, new DistanceCondition(true,
				"Station:Position must be within 1 km of all subordinate Channel:Position.", 1), Station.class);
		add(223, new StationElevationCondition(true,
				"Station:Elevation must be within 1 km of all subordinate Channel:Elevation."), Station.class);

	}

	private void defaultChannelRules(List<Integer> ignoreRules) {
		String codeRegex = "[A-Z0-9_\\*\\?]{3}";
		Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };
		add(301, new CodeCondition(true, codeRegex,
				"Channel:Code must be assigned a string consisting of 3 uppercase characters A-Z and or numeric characters 0-9."),
				Channel.class);
		add(302, new LocationCodeCondition(true, "([A-Z0-9\\*\\ ]{0,2})?",
				"Channel:locationCode must be unassigned or be assigned a string consisting of 0-2 uppercase characters A-Z and or numeric characters 0-9."),
				Channel.class);

		add(303, new CalibrationUnitCondition(false, "Invalid Calibration unit is invalid"), Channel.class);
		add(304, new SensorCondition(true, "Channel:Sensor:Description cannot be null."), Channel.class);
		add(305, new SampleRateCondition(false,
				"If Channel:SampleRate is NULL or 0 then Response information should not be included.", restrictions),
				Channel.class);

		add(310, new StartTimeCondition(true,
				"Channel:startDate is required and must occur before Channel:endDate if Channel:endDate is available."),
				Channel.class);
		add(320, new LatitudeCondition(true,
				"Channel:Latitude must be assigned a value that is not 0 and between -90 and 90."), Channel.class);
		add(321, new LongitudeCondition(true,
				"Channel:Longitude must be assigned a value that is not 0 and between -180 and 180."), Channel.class);

		add(330, new AzimuthCondition(true, "Azimuth must be assigned a value between 0 and 360.", 0, 360),
				Channel.class);

		add(331, new DipCondition(true, "Dip must be assigned a value between -90 and 90.", -90, 90), Channel.class);
		add(332, new OrientationCondition(true,
				"Channel:Azimuth and or Channel:Dip do not correspond within 5 degrees of tolerance to last digit of orthogonal Channel:Code.",
				new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() }), Channel.class);
	}

	private void defaultResponseRules(List<Integer> ignoreRules) {

		Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(), new ChannelTypeRestriction() };

		add(401, new StageSequenceCondition(true,
				"The 'number' attribute of Response::Stage element must start at 1 and be sequential", restrictions),
				Response.class);
		add(402, new UnitCondition(true,
				"Stage[N]:InputUnits:Name and/or Stage[N]:OutputUnits:Name are not defined in Unit name overview for IRIS StationXML validator.",
				restrictions), Response.class);
		add(403, new StageUnitCondition(true, "Stage[N]:InputUnits:Name must equal Stage[N-1]:OutputUnits:Name.",
				restrictions), Response.class);
		add(404, new DigitalFilterCondition(true,
				"Stage types FIR|Coefficient|PolesZeros with transfer function type Digital must include Decimation and StageGain elements.",
				restrictions), Response.class);
		add(405, new ResponseListCondition(true,
				"Stage of type ResponseList cannot be the only stage available in a response.",
				new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);

		add(410, new EmptySensitivityCondition(true, "InstrumentSensitivity:Value cannot be assigned 0 or Null.",
				new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
				Response.class);

		add(411, new FrequencyCondition(true,
				"InstrumentSensitivity:Frequency must be less than Channel:SampleRate/2 [Nyquist Frequency].",
				new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
				Response.class);
		add(412, new StageGainProductCondition(true,
				"InstrumentSensitivity:Value must equal the product of all StageGain:Value if all StageGain:Frequency are equal to InstrumentSensitivity:Frequency [Normalization Frequency].	",
				new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
				Response.class);
		add(413, new StageGainNonZeroCondition(true, "StageGain:Value cannot be assigned 0 or Null.",
				new ChannelCodeRestriction(), new ResponsePolynomialRestriction(), new ChannelTypeRestriction()),
				Response.class);

		add(414, new PolesZerosCondition(false,
				"If Stage[N] of type PolesZeros contains a Zero where both Real and Imaginary components equal 0 then InstrumentSensitivity:Frequency cannot equal 0 and Stage[N]:StageGain:Frequency cannot equal 0.",
				new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
				Response.class);

		add(415, new PolynomialCondition(false,
				"Response must be defined as Response:InstrumentPolynomial if it contains any Stages defined as ResponseStage:Polynomial",
				new ChannelCodeRestriction(), new ChannelTypeRestriction()), Response.class);

		add(420, new MissingDecimationCondition(true,
				"A Response must contain at least one instance of Response:Stage:Decimation.",
				new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
				Response.class);
		add(421, new DecimationSampleRateCondition(true,
				"Stage[Final]:Decimation:InputSampleRate divided by Stage[Final]:Decimation:Factor must equal Channel:SampleRate.",
				new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
				Response.class);
		add(422, new DecimationCondition(true,
				"Stage[N]:Decimation:InputSampleRate must equal Stage[N-1]:Decimation:InputSampleRate divided by Stage[N-1]:Decimation:Factor.",
				new ChannelCodeRestriction(), new ChannelTypeRestriction(), new ResponsePolynomialRestriction()),
				Response.class);
	}

	public void add(int id, Condition condition, Class<?> clazz) {
		if (condition == null || clazz == null) {
			throw new IllegalArgumentException("Null condition|class is not permitted");
		}
		Rule ruleToAdd = new Rule(id);
		ruleToAdd.setCondition(condition);
		add(ruleToAdd, clazz);
	}

	public void add(Rule ruleToAdd, Class<?> clazz) {
		if (ruleToAdd == null || clazz == null) {
			throw new IllegalArgumentException("Null rule|class is not permitted");
		}
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

	public Collection<Rule> getNetworkRules() {
		return this.networkRules.values();
	}

	public Collection<Rule> getStationRules() {
		return this.stationRules.values();
	}

	public Collection<Rule> getChannelRules() {
		return this.channelRules.values();
	}

	public Collection<Rule> getResponseRules() {
		return this.responseRules.values();
	}

}
