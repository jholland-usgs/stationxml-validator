package edu.iris.dmc.station.conditions;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class StageUnitCondition extends ChannelRestrictedCondition {

	public StageUnitCondition(boolean required, String description, Restriction[] restrictions) {
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
		return this.evaluate(channel, channel.getResponse());
	}

	@Override
	public Message evaluate(Channel channel, Response response) {
		if (isRestricted(channel)) {
			return Result.success();
		}

		if (this.required) {
			if (response == null) {
				return Result.error("expected response but was null");
			}
		}
		if (response.getStage() != null && !response.getStage().isEmpty()) {
			StageUnit current = null;

			for (ResponseStage stage : response.getStage()) {
				StageUnit stageUnit = getUnits(stage);
				if (stageUnit == null) {
					// return Result.error("stage [ null units for stage " +
					// stage.getNumber().intValue() + "]");
				} else {

					if (current != null && current.output != null && stageUnit.input != null) {
						if (!current.output.getName().equals(stageUnit.input.getName())) {
							// Stage[N]:InputUnits:Name must equal Stage[N-1]:OutputUnits:Name
							return Result.error("stage [" + stage.getNumber().intValue() + "] "
									+ stageUnit.input.getName() + " does not equal stage["
									+ (stage.getNumber().intValue() - 1) + "] " + stageUnit.output.getName());
						}
					}
				}
				if (stageUnit != null) {
					current = stageUnit;
				}
			}
		}
		return Result.success();
	}

	public StageUnit getUnits(ResponseStage stage) {

		Units input = null;
		Units output = null;
		if (stage.getPolesZeros() != null) {
			input = stage.getPolesZeros().getInputUnits();
			output = stage.getPolesZeros().getOutputUnits();
		}
		if (stage.getResponseList() != null) {
			input = stage.getResponseList().getInputUnits();
			output = stage.getResponseList().getOutputUnits();
		}
		if (stage.getFIR() != null) {
			input = stage.getFIR().getInputUnits();
			output = stage.getFIR().getOutputUnits();
		}
		if (stage.getPolynomial() != null) {
			input = stage.getPolynomial().getInputUnits();
			output = stage.getPolynomial().getOutputUnits();
		}
		if (stage.getCoefficients() != null) {
			input = stage.getCoefficients().getInputUnits();
			output = stage.getCoefficients().getOutputUnits();
		}

		return new StageUnit(input, output);
	}

	class StageUnit {
		Units input;
		Units output;

		public StageUnit(Units input, Units output) {
			this.input = input;
			this.output = output;
		}
	}
}
