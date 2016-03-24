package edu.iris.dmc.validation;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Gain;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Polynomial;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Sensitivity;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.validator.ResponseGroup;

public class ValidatorServiceImp implements ValidatorService {

	private javax.validation.Validator validator;

	public ValidatorServiceImp(javax.validation.Validator validator) {
		this.validator = validator;
	}

	public void run(List<Network> list, LEVEL level, Errors errors) {
		for (Network network : list) {
			Set<ConstraintViolation<Network>> constraintViolations = validator.validate(network);
			for (ConstraintViolation<Network> violation : constraintViolations) {
				errors.add(network.getCode(), network.getStartDate(), network.getEndDate(), null, null, null, null,
						null, null, null, violation.getMessage());
			}
			if (level.getValue() >= LEVEL.NETWORK.getValue()) {
				for (Station station : network.getStations()) {
					Set<ConstraintViolation<Station>> stationConstraintViolations = validator.validate(station);
					for (ConstraintViolation<Station> violation : stationConstraintViolations) {
						errors.add(network.getCode(), network.getStartDate(), network.getEndDate(), station.getCode(),
								station.getStartDate(), station.getEndDate(), null, null, null, null,
								violation.getMessage());
					}
					if (level.getValue() >= LEVEL.STATION.getValue()) {
						for (Channel channel : station.getChannels()) {
							Set<ConstraintViolation<Channel>> channelConstraintViolations = null;
							if (level.getValue() > LEVEL.CHANNEL.getValue()) {
								channelConstraintViolations = validator.validate(channel, Default.class,
										ResponseGroup.class);
							} else {
								channelConstraintViolations = validator.validate(channel);
							}
							for (ConstraintViolation<Channel> violation : channelConstraintViolations) {
								errors.add(network.getCode(), network.getStartDate(), network.getEndDate(),
										station.getCode(), station.getStartDate(), station.getEndDate(),
										channel.getLocationCode(), channel.getCode(), channel.getStartDate(),
										channel.getEndDate(), violation.getMessage());
							}
							if (level.getValue() >= LEVEL.CHANNEL.getValue()) {
								Response response = channel.getResponse();
								if (response == null) {
									// not sure what to do, what kind of
									// rules here?????
								}
								Set<ConstraintViolation<Response>> responseConstraintViolations = validator
										.validate(response);
								for (ConstraintViolation<Response> violation : responseConstraintViolations) {
									errors.add(network.getCode(), network.getStartDate(), network.getEndDate(),
											station.getCode(), station.getStartDate(), station.getEndDate(),
											channel.getLocationCode(), channel.getCode(), channel.getStartDate(),
											channel.getEndDate(), violation.getMessage());
								}

								if (response.getInstrumentSensitivity() != null) {
									Set<ConstraintViolation<Sensitivity>> stageConstraintViolations = validator
											.validate(response.getInstrumentSensitivity());
									for (ConstraintViolation<Sensitivity> violation : stageConstraintViolations) {
										errors.add(network.getCode(), network.getStartDate(), network.getEndDate(),
												station.getCode(), station.getStartDate(), station.getEndDate(),
												channel.getLocationCode(), channel.getCode(), channel.getStartDate(),
												channel.getEndDate(), violation.getMessage());
									}
								}

								if (response.getInstrumentPolynomial() != null) {
									Set<ConstraintViolation<Polynomial>> stageConstraintViolations = validator
											.validate(response.getInstrumentPolynomial());
									for (ConstraintViolation<Polynomial> violation : stageConstraintViolations) {
										errors.add(network.getCode(), network.getStartDate(), network.getEndDate(),
												station.getCode(), station.getStartDate(), station.getEndDate(),
												channel.getLocationCode(), channel.getCode(), channel.getStartDate(),
												channel.getEndDate(), violation.getMessage());
									}
								}

								for (ResponseStage stage : response.getStage()) {
									Set<ConstraintViolation<ResponseStage>> stageConstraintViolations = validator
											.validate(stage);
									for (ConstraintViolation<ResponseStage> violation : stageConstraintViolations) {
										errors.add(network.getCode(), network.getStartDate(), network.getEndDate(),
												station.getCode(), station.getStartDate(), station.getEndDate(),
												channel.getLocationCode(), channel.getCode(), channel.getStartDate(),
												channel.getEndDate(), violation.getMessage());
									}
								}
							}

						}
					}
				}
			}
		}
	}
}
