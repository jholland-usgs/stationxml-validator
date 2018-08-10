package edu.iris.dmc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.conditions.CodeConditionTest;
import edu.iris.dmc.station.conditions.DecimationCondition422Test;
import edu.iris.dmc.station.conditions.DecimationSampleRateCondition421Test;
import edu.iris.dmc.station.conditions.EmptySensitivity410Test;
import edu.iris.dmc.station.conditions.OrientationConditionTest;
import edu.iris.dmc.station.conditions.PolesZerosCondition414Test;
import edu.iris.dmc.station.conditions.ResponseList405Test;
import edu.iris.dmc.station.conditions.StageGainNonZeroCondition413Test;
import edu.iris.dmc.station.conditions.StageSequenceConditionTest;
import edu.iris.dmc.station.conditions.StageUnitConditionTest;
import edu.iris.dmc.station.conditions.StationEpochOverlapConditionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ RuleEngineServiceTest.class, CodeConditionTest.class, StationEpochOverlapConditionTest.class,DecimationCondition422Test.class,
		DecimationSampleRateCondition421Test.class, EmptySensitivity410Test.class, OrientationConditionTest.class,ResponseList405Test.class,
		PolesZerosCondition414Test.class, StageSequenceConditionTest.class,
		StageUnitConditionTest.class, StageGainNonZeroCondition413Test.class })
public final class AllTestsSuite {
}
