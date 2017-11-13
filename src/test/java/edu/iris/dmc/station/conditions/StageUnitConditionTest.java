package edu.iris.dmc.station.conditions;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Coefficients;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.rules.Result;

public class StageUnitConditionTest {

	private FDSNStationXML theDocument;

	@Before
	public void init() throws Exception {

		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("test.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
		}
	}

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		Network iu = theDocument.getNetwork().get(0);
		Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

		StageUnitCondition condition = new StageUnitCondition(true, "");
		Response response = bhz00.getResponse();
		Result result = condition.evaluate(bhz00,response);
		Assert.assertTrue(result.isSuccess());

		List<ResponseStage> stages = response.getStage();
		ResponseStage stage = stages.get(1);
		Assert.assertEquals(2, stage.getNumber().intValue());
		Coefficients coefficients = stage.getCoefficients();
		Units originalUnits = coefficients.getOutputUnits();
		Units units = new Units();
		units.setName("Dummy");
		units.setDescription("Dummy");
		coefficients.setOutputUnits(units);

		result = condition.evaluate(bhz00,response);
		Assert.assertFalse(result.isSuccess());

		coefficients.setOutputUnits(originalUnits);

		result = condition.evaluate(bhz00,response);
		Assert.assertTrue(result.isSuccess());

	}
}
