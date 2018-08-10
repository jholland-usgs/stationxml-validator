package edu.iris.dmc.station.conditions;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Success;

public class OrientationConditionTest {

	private FDSNStationXML theDocument;

	@Test
	public void n() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F1_332.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationCondition condition = new OrientationCondition(true, "");
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		}

	}
	@Test
	public void e() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F2_332.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationCondition condition = new OrientationCondition(true, "");
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		}

	}
	@Test
	public void z() throws Exception {

		try (InputStream is = OrientationConditionTest.class.getClassLoader().getResourceAsStream("F3_332.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			Network iu = theDocument.getNetwork().get(0);
			Station anmo = iu.getStations().get(0);
			OrientationCondition condition = new OrientationCondition(true, "");
			Channel channel = anmo.getChannels().get(0);
			Message result = condition.evaluate(channel);
			Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		}

	}
}
