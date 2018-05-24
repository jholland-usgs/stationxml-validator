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
import edu.iris.dmc.fdsn.station.model.SampleRate;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.Success;

public class SampleRateConditionTest {

	private FDSNStationXML theDocument;

	@Before
	public void init() throws Exception {

		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("408.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
		}
	}

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		Network iu = theDocument.getNetwork().get(0);
		Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

		DecimationCondition condition = new DecimationCondition(true, "");
		Response response = bhz00.getResponse();
		Message result = condition.evaluate(bhz00, response);

		result = condition.evaluate(bhz00, response);
		Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		
		SampleRate sr=bhz00.getSampleRate();
		sr.setValue(20);
		
		bhz00.setSampleRate(sr);
		
		result = condition.evaluate(bhz00, response);
		Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Success);


	}
}
