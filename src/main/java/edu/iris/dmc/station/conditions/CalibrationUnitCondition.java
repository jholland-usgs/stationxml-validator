package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.UnitTable;

public class CalibrationUnitCondition extends AbstractCondition {

	public CalibrationUnitCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Result evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Result evaluate(Channel channel) {
		Units units = channel.getCalibrationUnits();
		if (this.required && units == null) {
			return Result.of(false, "expected a value for calibration unit but was null");
		}

		if (units.getName() == null) {
			return Result.of(false, "expected a value for calibration unit/name but was null");
		}

		boolean result = UnitTable.contains(units.getName());
		if (result) {
			return Result.of(true, null);
		}

		return Result.of(false, "invalid value "+units.getName()+" for calibration unit");
	}
}
