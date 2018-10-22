package edu.iris.dmc.station;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import edu.iris.dmc.DocumentMarshaller;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.station.actions.DefaultAction;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.RuleContext;

public class RuleEngineServiceTest {

	private String datePattern = "yyyy-MM-dd'T'HH:mm:ss";

	private FDSNStationXML theDocument;

	@Test
	public void shouldRunWithNoProblems() throws Exception {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("pass.xml")) {
			theDocument = DocumentMarshaller.unmarshal(is);
			RuleEngineService ruleEngineService = new RuleEngineService(null);
			RuleContext context = getContext(true);
			for (Network network : theDocument.getNetwork()) {
				ruleEngineService.executeNetworkRules(network, context, new DefaultAction());
			}
			List<Message> resultSet = context.list();
			Assert.assertTrue("Expected result of rule execution to be true", resultSet.isEmpty());
		}
	}

	@Test
	public void rule111() throws Exception {

		theDocument = unmarshal("F1_111.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(111);
		assertNotNull(m);
		assertEquals(1, m.size());
		Message message = m.get(0);
		assertEquals(111, message.getRule().getId());
	}

	@Test
	public void rule112() throws Exception {

		theDocument = unmarshal("F1_112.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(112);
		assertNotNull(m);
		Message message = m.get(0);
		System.out.println(message.getRule().getId());
	}

	@Test
	public void rule211() throws Exception {

		theDocument = unmarshal("F1_211.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		Map<Integer, List<Message>> map = context.map();
		System.out.println(map);
		List<Message> m = context.getMessages(211);
		assertNotNull(m);
		Message message = m.get(0);
		assertEquals(211, message.getRule().getId());
	}

	@Test
	public void rule212() throws Exception {

		theDocument = unmarshal("F1_212.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(212);
		assertNotNull(m);
		Message message = m.get(0);
		assertEquals(212, message.getRule().getId());
	}

	@Test
	public void rule222() throws Exception {

		theDocument = unmarshal("F1_222.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(222);
		assertNotNull(m);
		Message message = m.get(0);
		assertEquals(222, message.getRule().getId());
	}

	@Test
	public void rule223() throws Exception {

		theDocument = unmarshal("F1_223.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(223);
		assertNotNull(m);
		Message message = m.get(0);
		assertEquals(223, message.getRule().getId());
	}

	@Test
	public void rule304() throws Exception {

		theDocument = unmarshal("F1_304.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(304);
		assertNotNull(m);
		Message message = m.get(0);
		assertEquals(304, message.getRule().getId());
	}

	@Test
	public void rule402() throws Exception {

		theDocument = unmarshal("F1_402.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(402);

		assertNotNull(m);
		Message message = m.get(0);
		assertEquals(402, message.getRule().getId());
	}

	@Test
	public void rule405() throws Exception {

		theDocument = unmarshal("F1_405.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(405);
		assertNotNull(m);
		Message message = m.get(0);
		assertEquals(405, message.getRule().getId());
	}

	@Test
	public void rule410() throws Exception {

		theDocument = unmarshal("F1_410.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> m = context.getMessages(410);
		assertNotNull(m);

		m = context.getMessages(412);
		assertNotNull(m);

		theDocument = unmarshal("F2_410.xml");
		ruleEngineService = new RuleEngineService(null);
		context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> resultSet = context.list();
		

		assertEquals(2, resultSet.size());

		assertNotNull(resultSet.get(0));

		assertEquals(410, resultSet.get(0).getRule().getId());

	}

	@Test
	public void rule411() throws Exception {

		theDocument = unmarshal("F1_411.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> resultSet = context.list();
		assertEquals(0, resultSet.size());

	}

	@Test
	public void rule412() throws Exception {

		theDocument = unmarshal("F1_412.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> resultSet = context.list();

		assertEquals(1, resultSet.size());

		Message message = resultSet.get(0);
		assertNotNull(message.getRule());

		assertEquals(412, message.getRule().getId());

	}

	@Test
	public void rule413() throws Exception {

		theDocument = unmarshal("F1_413.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> resultSet = context.list();
		assertEquals(1, resultSet.size());

		List<Message> list = context.getMessages(413);
		assertNotNull(list);
		Message m = list.get(0);
		assertEquals(413, m.getRule().getId());

		list = context.getMessages(413);
			
		assertNotNull(list);
		m = list.get(0);
		assertEquals(413, m.getRule().getId());

	}

	@Test
	public void rule414() throws Exception {

		theDocument = unmarshal("F1_414.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> resultSet = context.list();
		//for (Message m : resultSet) {
			// System.out.println("414: "+m);
		//}
		assertEquals(1, resultSet.size());

		List<Message> list = context.getMessages(414);
		assertNotNull(list);
		Message m = list.get(0);
		assertEquals(414, m.getRule().getId());

		list = context.getMessages(414);
		assertNotNull(list);

	}

	@Test
	public void rule421() throws Exception {

		theDocument = unmarshal("F1_421.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> resultSet = context.list();
		assertEquals(1, resultSet.size());

		List<Message> list = context.getMessages(421);
		assertNotNull(list);
		Message m = list.get(0);
		assertEquals(421, m.getRule().getId());

	}

	@Test
	public void rule422() throws Exception {

		theDocument = unmarshal("F1_422.xml");
		RuleEngineService ruleEngineService = new RuleEngineService(null);
		RuleContext context = getContext(true);
		ruleEngineService.executeAllRules(theDocument, context, new DefaultAction());

		List<Message> resultSet = context.list();
		assertEquals(1, resultSet.size());

		List<Message> list = context.getMessages(422);
		assertNotNull(list);
		Message m = list.get(0);
		assertEquals(422, m.getRule().getId());

	}

	private FDSNStationXML unmarshal(String file) throws JAXBException, IOException {
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream(file)) {
			return DocumentMarshaller.unmarshal(is);
		}
	}

	private RuleContext getContext(boolean bool) {
		return RuleContext.of(bool);
	}

}
