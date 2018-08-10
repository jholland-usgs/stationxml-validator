package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.UnitTable;

public class CalibrationUnitCondition extends AbstractCondition {

	public CalibrationUnitCondition(boolean required, String description) {
		super(required, description);
	}

	@Override
	public Message evaluate(Network network) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Station station) {
		throw new IllegalArgumentException("Not supported!");
	}

	@Override
	public Message evaluate(Channel channel) {
		Units units = channel.getCalibrationUnits();
		if (units == null) {
			if (this.required) {
				return Result.error("expected a value for calibration unit but was null");
			}else{
				return Result.success();
			}
			
		}

		if (units.getName() == null) {
			return Result.error("expected a value for calibration unit/name but was null");
		}

		boolean result = UnitTable.contains(units.getName());
		if (result) {
			return Result.success();
		}

		result = UnitTable.containsCaseInsensitive(units.getName().toLowerCase());
		if (result) {
			return Result.warning("expected " + units.getName().toLowerCase() + " for calibration unit/name but was "
					+ units.getName());
		}

		return Result.error("Invalid unit " + units.getName());
	}
}
