package edu.iris.dmc.service;

import java.util.List;
import java.util.Set;

import edu.iris.dmc.fdsn.station.model.LEVEL;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.validation.rule.Rule;
import edu.iris.dmc.validation.rule.UnitTable;

public interface ValidatorService {
	public Errors run(List<Network> list, LEVEL level, List<Integer> ignore);

	public List<Rule>  getRules();

	public UnitTable getUnits();
}
