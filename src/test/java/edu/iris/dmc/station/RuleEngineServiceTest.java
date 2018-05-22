package edu.iris.dmc.station;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.BaseNodeType.LEVEL;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Latitude;
import edu.iris.dmc.fdsn.station.model.Longitude;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.station.actions.DefaultAction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.RuleContext;

public class RuleEngineServiceTest {

	private String datePattern = "yyyy-MM-dd'T'HH:mm:ss";

	private FDSNStationXML theDocument;

	@Before
	public void init() throws Exception {

		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("test.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
		}
	}

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		RuleEngineService ruleEngineService = new RuleEngineService();
		RuleContext context = getContext(LEVEL.RESPONSE);
		for (Network network : theDocument.getNetwork()) {
			ruleEngineService.executeNetworkRules(network, context, new DefaultAction());
		}
		List<Message> resultSet = context.getResponse();
		Assert.assertTrue("Expected result of rule execution to be true", resultSet.isEmpty());
	}

	@Test
	public void networkCode() throws Exception {
		RuleEngineService ruleEngineService = new RuleEngineService();
		RuleContext context = getContext(LEVEL.RESPONSE);
		for (Network network : theDocument.getNetwork()) {
			network.setCode("IUUUUUUUUU");
			ruleEngineService.executeNetworkRules(network, context, new DefaultAction());
		}
		List<Message> resultSet = context.getResponse();
		Assert.assertFalse("Expected result of rule execution to be true", resultSet.isEmpty());

		for (Message r : resultSet) {
			System.out.println(r);
		}
		Assert.assertEquals(1, resultSet.size());
		Message result = resultSet.get(0);
		Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		Assert.assertEquals(101, result.getRule().getId());
	}

	@Test
	public void networkTimeRange() throws Exception {
		RuleEngineService ruleEngineService = new RuleEngineService();
		RuleContext context = getContext(LEVEL.RESPONSE);

		Network network = theDocument.getNetwork().get(0);
		Date cal = XmlUtil.toDate("yyyy-MM-dd'T'HH:mm:ss", "2502-12-31T23:59:59");

		network.setStartDate(cal);
		System.out.println(network.getStartDate() + "  " + network.getEndDate());
		ruleEngineService.executeNetworkRules(network, context, new DefaultAction());

		List<Message> resultSet = context.getResponse();
		Assert.assertFalse("Expected result of rule execution to be true", resultSet.isEmpty());
		Assert.assertEquals(1, resultSet.size());
		Message result = resultSet.get(0);
		Assert.assertTrue(result instanceof edu.iris.dmc.station.rules.Error);
		Assert.assertEquals(105, result.getRule().getId());

		context.clear();
		network.getStations().get(0).setStartDate(cal);
		ruleEngineService.executeNetworkRules(network, context, new DefaultAction());
		resultSet = context.getResponse();
		Assert.assertFalse("Expected result of rule execution to be true", resultSet.isEmpty());
		Assert.assertEquals(1, resultSet.size());

		resultSet = context.getResponse(105);
		Assert.assertFalse(resultSet.isEmpty());
		result = resultSet.get(0);
		Assert.assertEquals(105, result.getRule().getId());

	}

	// @Test
	public void testNetworkCodeDate() throws Exception {

		Network network = getNetwork();
		RuleEngineService ruleEngineService = new RuleEngineService();
		RuleContext context = getContext(LEVEL.RESPONSE);
		ruleEngineService.executeNetworkRules(network, context, new DefaultAction());
		List<Message> resultSet = context.getResponse();
		Assert.assertTrue("Expected result of rule execution to be true", resultSet.isEmpty());

		network.setEndDate(XmlUtil.toDate("yyyy-MM-dd'T'HH:mm:ss", "2008-01-01T10:00:00"));
		context.clear();
		ruleEngineService.executeNetworkRules(network, context, new DefaultAction());
		resultSet = context.getResponse();
		Assert.assertFalse("Expected result of rule execution to be flase", resultSet.isEmpty());
		Assert.assertEquals(1, resultSet.size());
	}

	// @Test
	public void testNetworkStationOverlap() throws Exception {

		Network iu = getNetwork();
		Station anmo1 = getStation();
		Station anmo2 = getStation();
		anmo2.setStartDate(XmlUtil.toDate(datePattern, "2010-11-01T10:00:00"));

		iu.addStation(anmo1);
		iu.addStation(anmo2);

		RuleEngineService ruleEngineService = new RuleEngineService();
		RuleContext context = getContext(LEVEL.RESPONSE);
		ruleEngineService.executeNetworkRules(iu, context, new DefaultAction());
		List<Message> resultSet = context.getResponse();
		Assert.assertFalse("Expected result of rule execution to be true", resultSet.isEmpty());
		Assert.assertEquals(1, resultSet.size());
	}

	private RuleContext getContext(LEVEL level) {
		return RuleContext.of(level);
	}

	private Network getNetwork() throws ParseException {
		Network network = new Network();
		network.setCode("IU");
		network.setStartDate(XmlUtil.toDate(datePattern, "2010-01-01T10:00:00"));
		network.setEndDate(XmlUtil.toDate(datePattern, "2012-01-01T10:00:00"));

		return network;
	}

	private Station getStation() throws ParseException {
		Station station = new Station();
		station.setCode("ANMO");

		station.setStartDate(XmlUtil.toDate(datePattern, "2010-01-01T10:00:00"));
		station.setEndDate(XmlUtil.toDate(datePattern, "2011-01-01T10:00:00"));
		station.setCreationDate(XmlUtil.toDate(datePattern, "2010-01-01T10:00:00"));
		Latitude latitude = new Latitude();
		latitude.setValue(30);
		station.setLatitude(latitude);

		Longitude longitude = new Longitude();
		longitude.setValue(30);
		station.setLongitude(longitude);

		return station;
	}
}
