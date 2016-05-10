package edu.iris.dmc.station.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Gain;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Sensitivity;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.validator.ResponseGroup;

public class SpecificRuleTest {

	private InputStream in;
	private static Properties messages = new Properties();
	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		InputStream is = null;
		try {
			is = SpecificRuleTest.class.getClassLoader().getResourceAsStream("ValidationMessages.properties");
			messages.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@After
	public void close() {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	

	@Test
	public void dateRange305() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_CHASTARTDATE_afterenddate.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network ii = networks.get(0);
		assertNotNull(ii);
		Set<ConstraintViolation<Network>> violations = validator.validate(ii);
		assertEquals(0, violations.size());
		List<Station> stations = ii.getStations();
		assertEquals(1, stations.size());
		Station kdak = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(kdak);
		assertEquals(0, stationViolations.size());

		assertNotNull(kdak.getChannels());
		assertFalse(kdak.getChannels().isEmpty());
		assertEquals(1, kdak.getChannels().size());
		Channel channel = kdak.getChannels().get(0);

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel);
		assertEquals(1, channelViolations.size());

		ConstraintViolation<Channel> violation = channelViolations.iterator().next();
		assertEquals(messages.get("channel.epoch.range"), violation.getMessage());

	}

	//@Test
	public void noOverlap() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("ANMO_BHZ_epoch_overlap.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network iu = networks.get(0);
		assertNotNull(iu);
		assertNotNull(iu.getStations());
		assertTrue(!iu.getStations().isEmpty());
		Set<ConstraintViolation<Network>> violations = validator.validate(iu);
		assertEquals(1, violations.size());
		ConstraintViolation<Network> violation = violations.iterator().next();
		assertEquals(messages.get("network.station.overlap"), violation.getMessage());

		Station anmo1 = iu.getStations().get(0);
		assertNotNull(anmo1);
		assertNotNull(anmo1.getChannels());
		assertTrue(!anmo1.getChannels().isEmpty());
		Set<ConstraintViolation<Station>> anmoViolations = validator.validate(anmo1);
		assertEquals(1, anmoViolations.size());
		ConstraintViolation<Station> v = anmoViolations.iterator().next();
		assertEquals(messages.get("station.channel.overlap"), v.getMessage());

	}

}
