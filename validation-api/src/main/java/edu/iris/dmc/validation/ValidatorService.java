package edu.iris.dmc.validation;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.Network;

public interface ValidatorService {
	public void run(List<Network> list, LEVEL level, Errors errors);
}
