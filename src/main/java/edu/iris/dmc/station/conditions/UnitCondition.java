package edu.iris.dmc.station.conditions;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.NestedMessage;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.Success;
import edu.iris.dmc.station.rules.UnitTable;

public class UnitCondition extends AbstractCondition {

	public UnitCondition(boolean required, String description) {
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
		if (channel == null || channel.getResponse() == null || channel.getResponse().getStage() == null) {
			// should be checked somewhere else
			return new Success("");
		}

		List<ResponseStage> stages = channel.getResponse().getStage();

		NestedMessage message = new NestedMessage();
		for (ResponseStage stage : stages) {
			Units[] units = getUnits(stage);
			if (units == null) {
				message.add(Result.error("stage [ null units for stage " + stage.getNumber().intValue() + "]"));
			} else {
				Units inputUnits = units[0];
				Units outputUnits = units[1];

				if (inputUnits == null || inputUnits.getName() == null) {
					message.add(Result.error("Inputunit cannot be null [stage "+stage.getNumber().intValue()+"]"));
				} else {
					boolean result = UnitTable.contains(inputUnits.getName());
					if (!result) {
						result = UnitTable.contains(inputUnits.getName().toLowerCase());
						if (result) {
							message.add(Result.warning("[stage "+stage.getNumber().intValue()+"] expected inputUnit " + inputUnits.getName().toLowerCase()
									+ " unit/name but was " + inputUnits.getName()));
						}else{
							message.add(Result.error("[stage "+stage.getNumber().intValue()+"] expected inputUnit " + inputUnits.getName().toLowerCase()
									+ " unit/name but was " + outputUnits.getName()));
						}
					}
				}

				if (outputUnits == null || outputUnits.getName() == null) {
					message.add(Result.error("Outputunit cannot be null [stage "+stage.getNumber().intValue()+"]"));
				} else {
					boolean result = UnitTable.contains(outputUnits.getName());
					if (!result) {
						result = UnitTable.contains(outputUnits.getName().toLowerCase());
						if (result) {
							message.add(Result.warning("[stage "+stage.getNumber().intValue()+"] expected outputUnit " + outputUnits.getName().toLowerCase()
									+ " unit/name but was " + outputUnits.getName()));
						}else{
							message.add(Result.error("[stage "+stage.getNumber().intValue()+"] expected outputUnit " + outputUnits.getName().toLowerCase()
									+ " unit/name but was " + outputUnits.getName()));
						}
					}
				}
			}
		}
		return message;
	}

	public Units[] getUnits(ResponseStage stage) {

		if (stage.getPolesZeros() != null) {
			return new Units[] { stage.getPolesZeros().getInputUnits(), stage.getPolesZeros().getOutputUnits() };
		}
		if (stage.getResponseList() != null) {
			return new Units[] { stage.getResponseList().getInputUnits(), stage.getResponseList().getOutputUnits() };
		}
		if (stage.getFIR() != null) {
			return new Units[] { stage.getFIR().getInputUnits(), stage.getFIR().getOutputUnits() };
		}
		if (stage.getPolynomial() != null) {
			return new Units[] { stage.getPolynomial().getInputUnits(), stage.getPolynomial().getOutputUnits() };
		}
		if (stage.getCoefficients() != null) {
			return new Units[] { stage.getCoefficients().getInputUnits(), stage.getCoefficients().getOutputUnits() };
		}
		return null;
	}
}
