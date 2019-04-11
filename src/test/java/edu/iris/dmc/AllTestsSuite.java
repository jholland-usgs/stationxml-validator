package edu.iris.dmc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.conditions.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({ RuleEngineServiceTest.class, CodeConditionTest.class, StationEpochOverlapConditionTest.class,DecimationCondition422Test.class,
		DecimationSampleRateCondition421Test.class, EmptySensitivity410Test.class, OrientationConditionTest.class,ResponseList405Test.class,
		PolesZerosCondition414Test.class, StageSequenceConditionTest.class,
		StageUnitConditionTest.class, StageGainNonZeroCondition413Test.class,
		Condition101Test.class,Condition110Test.class,Condition110Test2.class,
		Condition111Pass.class,Condition111Test.class,Condition111Test2.class,
		Condition112Pass.class,Condition112Test.class,
		Condition201Test.class,Condition210Test.class,Condition210Test2.class,
		Condition211Pass.class,Condition211Test.class,Condition212Test.class,
		Condition220Test.class,Condition221Test.class,Condition222Test.class,
		Condition223Test.class,Condition301Test.class,Condition302Test.class,
		Condition302Test2.class,Condition303Test.class,Condition304Test.class,
		Condition305Pass.class,Condition305Test.class,Condition305Test2.class,
		Condition310Test.class,Condition310Test2.class,Condition320Test.class,
		Condition321Test.class,Condition330Test.class,Condition331Test.class,
		Condition332Pass.class,Condition332Pass2.class,Condition332Pass3.class,
		Condition332Pass4.class,Condition332Test.class,Condition332Test2.class,
		Condition332Test3.class,Condition401Test.class,Condition402Test.class,
		Condition403Test.class,Condition403Test2.class,Condition404Pass.class,
		Condition404Test.class,Condition404Test2.class,Condition404Test3.class,
		Condition404Test4.class,Condition405Pass.class,Condition405Test.class,
		Condition410Test.class,Condition410Test2.class,Condition411Test.class,
		Condition412Pass.class,Condition412Pass2.class,Condition412Test.class, 
		Condition413Pass.class,Condition413Test.class,Condition414Pass.class,
		Condition414Test.class,Condition414Test2.class,Condition414Test3.class,
		Condition415Pass.class,Condition415Pass2.class,Condition415Test.class,
		Condition420Test.class,Condition421Test.class,Condition422Test.class})
public final class AllTestsSuite {
}
