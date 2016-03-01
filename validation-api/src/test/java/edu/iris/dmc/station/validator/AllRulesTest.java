package edu.iris.dmc.station.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
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
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;

public class AllRulesTest {

	private static Validator validator;
	private InputStream in;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
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
	public void networkCodeTooLong() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_lengthNETCODE.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);

		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(1, violations.size());

		ConstraintViolation<Network> violation = violations.iterator().next();
		assertEquals("IIII", violation.getInvalidValue());
		assertEquals("102, network code doesn't match [A-Za-z0-9\\*\\?]{1,2}", violation.getMessage());
	}

	@Test
	public void networkCodeWrong() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_charNETCODE.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);

		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(1, violations.size());

		ConstraintViolation<Network> violation = violations.iterator().next();
		assertEquals("#$", violation.getInvalidValue());
		assertEquals("102, network code doesn't match [A-Za-z0-9\\*\\?]{1,2}", violation.getMessage());
	}

	@Test
	public void networkInvalidStartTime() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_badNetSTARTTIME.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);

		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(1, violations.size());

		ConstraintViolation<Network> violation = violations.iterator().next();
		assertEquals("105, starttime should be before endtime", violation.getMessage());
	}

	@Test
	public void nullStationCode() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_nullSTACODE.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(1, stationViolations.size());
		ConstraintViolation<Station> violation = stationViolations.iterator().next();
		assertEquals("202, station code doesn't match [A-Za-z0-9\\*\\?]{1,5}", violation.getMessage());

		station.setCode("ANMO");
		stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());
	}

	@Test
	public void nullChannelCode() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_nullCHACODE.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());
		List<Channel> channels = stations.get(0).getChannels();

		for (Channel channel : channels) {
			Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel);
			for (ConstraintViolation<Channel> v : channelViolations) {
				System.out.println("out: " + v.getMessage());
			}
			assertEquals(1, channelViolations.size());

			Iterator<ConstraintViolation<Channel>> it = channelViolations.iterator();
			assertTrue(it.hasNext());
			ConstraintViolation<Channel> violation = it.next();
			assertEquals("[SEED:b52,4] [] doesn't match $[A-Za-z0-9\\*\\?]{1,3}", violation.getMessage());
			while (it.hasNext()) {
				System.out.println(it.next().getMessage());
			}

			channel.setCode("BHZ");
			channelViolations = validator.validate(channel);
			for (ConstraintViolation<Channel> v : channelViolations) {
				System.out.println(v.getMessage());
			}
			assertEquals(0, channelViolations.size());
		}
	}

	@Test
	public void nulllocationCode() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_nullLOCCODE.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());
		List<Channel> channels = stations.get(0).getChannels();
		assertEquals(3, channels.size());
		for (Channel channel : channels) {
			Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel);
			assertEquals(0, channelViolations.size());

		}
	}

	@Test
	public void channelTooFarFromStation() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_CHAtooFarFromSTA.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		in.close();
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(1, stationViolations.size());
		ConstraintViolation<Station> violation = stationViolations.iterator().next();
		assertEquals("Channel ditsnace from the station shouldn't exceed 1 KM", violation.getMessage());
	}

	@Test
	public void stageSequence() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("AUMCQBHZ_stageNOSEQUENCE.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());

		assertNotNull(station.getChannels());
		assertFalse(station.getChannels().isEmpty());
		assertEquals(1, station.getChannels().size());
		Channel channel = station.getChannels().get(0);

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel);
		assertEquals(0, channelViolations.size());

		assertNotNull(channel.getResponse());
		assertNotNull(channel.getResponse().getStage());
		assertEquals(5, channel.getResponse().getStage().size());

		Set<ConstraintViolation<Response>> responseViolations = validator.validate(channel.getResponse());
		assertEquals(2, responseViolations.size());
		ConstraintViolation<Response> violation = responseViolations.iterator().next();
		assertEquals("Stage number attribute must start at 1, be present in numerical order and have no gaps",
				violation.getMessage());

	}

	@Test
	public void nonZeroSampleRate() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_emptyINSTSENSITIVITY.xml");
		FDSNStationXML root = (FDSNStationXML) jaxbUnmarshaller.unmarshal(in);

		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());

		assertNotNull(station.getChannels());
		assertFalse(station.getChannels().isEmpty());
		assertEquals(1, station.getChannels().size());
		Channel channel = station.getChannels().get(0);

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel);
		assertEquals(1, channelViolations.size());

	}

	@Test
	public void invalidAzimuth() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();

		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_invalidCHAAZ.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());

		assertNotNull(station.getChannels());
		assertFalse(station.getChannels().isEmpty());
		assertEquals(3, station.getChannels().size());
		Channel channel = station.getChannels().get(0);

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel);
		// System.out.println("??????"+channelViolations.iterator().next().getMessage());
		assertEquals(1, channelViolations.size());

	}

	@Test
	public void digitialTransferFunction() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_digitalTransferFxn_NoDecimation.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(0, violations.size());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());

		assertNotNull(station.getChannels());
		assertFalse(station.getChannels().isEmpty());
		assertEquals(1, station.getChannels().size());
		Channel channel = station.getChannels().get(0);

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel);
		// System.out.println("??????"+channelViolations.iterator().next().getMessage());
		assertEquals(0, channelViolations.size());

		assertNotNull(channel.getResponse());
		List<ResponseStage> stages = channel.getResponse().getStage();
		assertEquals(6, stages.size());

		for (ResponseStage stage : stages) {
			Set<ConstraintViolation<ResponseStage>> stageViolations = validator.validate(stage);
			if (stage.getNumber().intValue() == 1) {
				assertEquals(0, stageViolations.size());
			} else if (stage.getNumber().intValue() == 2) {
				assertEquals(0, stageViolations.size());
			} else if (stage.getNumber().intValue() == 3) {
				assertEquals(1, stageViolations.size());
			} else if (stage.getNumber().intValue() == 4) {
				assertEquals(1, stageViolations.size());
			} else if (stage.getNumber().intValue() == 5) {
				assertEquals(1, stageViolations.size());
			} else if (stage.getNumber().intValue() == 6) {
				assertEquals(1, stageViolations.size());
			}
		}
	}

	@Test
	public void mismatchedUnits() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_StageunitsNoMatch.xml");
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
		// System.out.println("??????"+channelViolations.iterator().next().getMessage());
		assertEquals(0, channelViolations.size());

		Response response = channel.getResponse();
		assertNotNull(response);
		Set<ConstraintViolation<Response>> responseViolations = validator.validate(response);
		assertEquals(1, responseViolations.size());
		ConstraintViolation<Response> violation = responseViolations.iterator().next();
		assertNotNull(violation);
		System.out.println(violation.getMessage());
		/*
		 * List<ResponseStage> stages = channel.getResponse().getStage();
		 * assertEquals(9, stages.size());
		 * 
		 * for (ResponseStage stage : stages) {
		 * Set<ConstraintViolation<ResponseStage>> stageViolations =
		 * validator.validate(stage); if (stage.getNumber().intValue() == 1) {
		 * assertEquals(0, stageViolations.size()); } else if
		 * (stage.getNumber().intValue() == 2) { assertEquals(0,
		 * stageViolations.size()); } else if (stage.getNumber().intValue() ==
		 * 3) { assertEquals(1, stageViolations.size()); } else if
		 * (stage.getNumber().intValue() == 4) { assertEquals(1,
		 * stageViolations.size()); } else if (stage.getNumber().intValue() ==
		 * 5) { assertEquals(1, stageViolations.size()); } else if
		 * (stage.getNumber().intValue() == 6) { assertEquals(1,
		 * stageViolations.size()); } }
		 */
	}

}
