package edu.iris.dmc.station.conditions;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;

public class CodeConditionTest {

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
		CodeCondition condition = new CodeCondition(true, "[A-Za-z0-9\\*\\?]{1,2}", "");
		Message result = condition.evaluate(iu);
		System.out.println(result.getDescription());
		Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Success);

		iu.setCode("IIIIII");
		result = condition.evaluate(iu);
		Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);

		

	}
}
