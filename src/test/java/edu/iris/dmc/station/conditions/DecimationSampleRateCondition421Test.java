package edu.iris.dmc.station.conditions;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.station.RuleEngineServiceTest;
import edu.iris.dmc.station.restrictions.ChannelCodeRestriction;
import edu.iris.dmc.station.restrictions.ChannelTypeRestriction;
import edu.iris.dmc.station.restrictions.Restriction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Success;

public class DecimationSampleRateCondition421Test {

	private FDSNStationXML theDocument;

	@Before
	public void init() throws Exception {

	}

	@Test
	public void fail() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_421.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network iu = theDocument.getNetwork().get(0);
			Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
					new ChannelTypeRestriction() };

			DecimationSampleRateCondition condition = new DecimationSampleRateCondition(true, "", restrictions);
			
			Response response = bhz00.getResponse();
			Message result = condition.evaluate(bhz00, response);
			Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		}

	}
	
	@Test
	public void pass() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);

			Network iu = theDocument.getNetwork().get(0);
			Channel bhz00 = iu.getStations().get(0).getChannels().get(0);

			Restriction[] restrictions = new Restriction[] { new ChannelCodeRestriction(),
					new ChannelTypeRestriction() };

			DecimationSampleRateCondition condition = new DecimationSampleRateCondition(true, "", restrictions);

			Message result = condition.evaluate(bhz00);
			Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Success);
		}

	}
}
