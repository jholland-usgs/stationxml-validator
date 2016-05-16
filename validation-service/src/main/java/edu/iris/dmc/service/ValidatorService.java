package edu.iris.dmc.validation;

import java.util.List;

import edu.iris.dmc.fdsn.station.model.LEVEL;
import edu.iris.dmc.fdsn.station.model.Network;

public interface ValidatorService {
	public Errors run(List<Network> list, LEVEL level, List<Integer> ignore);
}
