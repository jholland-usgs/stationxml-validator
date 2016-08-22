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
import edu.iris.dmc.fdsn.station.model.Equipment;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Gain;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Sensitivity;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.validator.ResponseGroup;

public class AllRulesTest {

	private InputStream in;
	private static Properties messages = new Properties();
	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		InputStream is = null;
		try {
			is = AllRulesTest.class.getClassLoader().getResourceAsStream("ValidationMessages.properties");
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
		assertEquals(messages.get("network.code.regex"), violation.getMessage());
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
		assertEquals(messages.get("network.code.regex"), violation.getMessage());
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
		assertEquals(messages.get("network.epoch.range"), violation.getMessage());
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
		ConstraintViolation<Station> v = stationViolations.iterator().next();
		assertEquals(1, stationViolations.size());
		ConstraintViolation<Station> violation = stationViolations.iterator().next();
		assertEquals(messages.get("station.code.regex"), violation.getMessage());

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
			assertEquals(1, channelViolations.size());

			Iterator<ConstraintViolation<Channel>> it = channelViolations.iterator();
			assertTrue(it.hasNext());
			ConstraintViolation<Channel> violation = it.next();
			assertEquals(messages.get("channel.code.regex"), violation.getMessage());
			channel.setCode("BHZ");
			channelViolations = validator.validate(channel);
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
	public void channelTooFarFromStation251() throws Exception {
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
		assertEquals(messages.get("station.channel.distance"), violation.getMessage());
	}

	@Test
	public void stationChannelElevation() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("station_channel_elevation.xml");
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
		assertEquals(2, stationViolations.size());
		ConstraintViolation<Station> violation1 = stationViolations.iterator().next();
		ConstraintViolation<Station> violation2 = stationViolations.iterator().next();
		//System.out.println(messages.get("station.elevation")+"  "+violation1.getMessage()+"   "+violation2.getMessage());
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
		assertEquals(1, responseViolations.size());
		ConstraintViolation<Response> violation1 = responseViolations.iterator().next();

		assertTrue("401,The 'number' attribute of Response::Stage element must start at 1 and be sequential"
				.equals(violation1.getMessage())
				|| "402, The element <InputUnits> of a stage must match the element <OutputUnits> of the preceding stage, except for stages 0 or 1"
						.equals(violation1.getMessage()));
	}

	@Test
	public void shouldPass402() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("402_pass.xml");
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

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel, ResponseGroup.class);
		assertEquals(0, channelViolations.size());

		assertNotNull(channel.getResponse());
		assertNotNull(channel.getResponse().getStage());
		assertEquals(6, channel.getResponse().getStage().size());
		ResponseStage stage = channel.getResponse().getStage().get(2);
		Set<ConstraintViolation<Response>> responseViolations = validator.validate(channel.getResponse());
		assertEquals(0, responseViolations.size());

	}

	@Test
	public void nonZeroSampleRate() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_INSTSENS_notlisted.xml");
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

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel, ResponseGroup.class);
		assertEquals(1, channelViolations.size());
		ConstraintViolation<Channel> violation = channelViolations.iterator().next();
		assertEquals(messages.get("response.samplerate.407"), violation.getMessage());

	}

	@Test
	public void nonZeroSampleRateNoStages() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		in = this.getClass().getClassLoader().getResourceAsStream("CIABLEHZ_block61NoCoeff.xml");
		FDSNStationXML root = (FDSNStationXML) jaxbUnmarshaller.unmarshal(in);

		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);
		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(1, violations.size());
		assertEquals(messages.get("network.starttime.notnull"), violations.iterator().next().getMessage());
		List<Station> stations = n.getStations();
		assertEquals(1, stations.size());
		Station station = stations.get(0);
		Set<ConstraintViolation<Station>> stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());

		assertNotNull(station.getChannels());
		assertFalse(station.getChannels().isEmpty());
		assertEquals(3, station.getChannels().size());
		Channel channel = station.getChannels().get(0);

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel, ResponseGroup.class);
		assertEquals(1, channelViolations.size());
		ConstraintViolation<Channel> violation = channelViolations.iterator().next();
		assertEquals(messages.get("response.samplerate.406"), violation.getMessage());

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
		assertEquals(0, channelViolations.size());

		Response response = channel.getResponse();
		assertNotNull(response);
		Set<ConstraintViolation<Response>> responseViolations = validator.validate(response);
		assertEquals(1, responseViolations.size());
		ConstraintViolation<Response> violation = responseViolations.iterator().next();
		assertNotNull(violation);

		assertEquals(messages.get("response.stage.unit"), violation.getMessage());
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

	@Test
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

	@Test
	public void zeroGain403() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_STAGEGAIN_zero.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network ii = networks.get(0);
		assertNotNull(ii);
		assertNotNull(ii.getStations());
		assertTrue(!ii.getStations().isEmpty());
		Set<ConstraintViolation<Network>> violations = validator.validate(ii);
		assertEquals(0, violations.size());

		Station kdak = ii.getStations().get(0);
		assertNotNull(kdak);
		assertNotNull(kdak.getChannels());
		assertTrue(!kdak.getChannels().isEmpty());
		Set<ConstraintViolation<Station>> v = validator.validate(kdak);
		assertEquals(0, v.size());

		assertNotNull(kdak.getChannels());
		assertTrue(!kdak.getChannels().isEmpty());

		Channel vhz = kdak.getChannels().get(0);
		Set<ConstraintViolation<Channel>> cv = validator.validate(vhz);
		assertEquals(0, cv.size());

		assertNotNull(vhz.getResponse().getStage());
		assertTrue(!vhz.getResponse().getStage().isEmpty());

		ResponseStage stage = vhz.getResponse().getStage().get(0);
		Gain gain = stage.getStageGain();
		assertNotNull(gain);
		Set<ConstraintViolation<Gain>> gv = validator.validate(gain);
		assertEquals(1, gv.size());

		ConstraintViolation<Gain> gvm = gv.iterator().next();
		assertEquals(messages.get("gain.value"), gvm.getMessage());

	}

	@Test
	public void zeroInstrumentSensetivity404() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("IIKDAK10VHZ_INSTSENS_zero.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network ii = networks.get(0);
		assertNotNull(ii);
		assertNotNull(ii.getStations());
		assertTrue(!ii.getStations().isEmpty());
		Set<ConstraintViolation<Network>> violations = validator.validate(ii);
		assertEquals(0, violations.size());

		Station kdak = ii.getStations().get(0);
		assertNotNull(kdak);
		assertNotNull(kdak.getChannels());
		assertTrue(!kdak.getChannels().isEmpty());
		Set<ConstraintViolation<Station>> v = validator.validate(kdak);
		assertEquals(0, v.size());

		assertNotNull(kdak.getChannels());
		assertTrue(!kdak.getChannels().isEmpty());

		Channel vhz = kdak.getChannels().get(0);
		Set<ConstraintViolation<Channel>> cv = validator.validate(vhz);
		assertEquals(0, cv.size());

		Sensitivity is = vhz.getResponse().getInstrumentSensitivity();
		assertNotNull(is);
		Set<ConstraintViolation<Gain>> gv = validator.validate(is);
		assertEquals(2, gv.size());
		// Iterator<ConstraintViolation<Gain>> it = gv.iterator();

		// ConstraintViolation<Gain> gvm = gv.iterator().next();
		// assertEquals(messages.get("gain.value"),gvm.getMessage());

	}

	@Test
	public void noCoeef_411() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("411_CI_ABL.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network network = networks.get(0);
		assertNotNull(network);
		assertNotNull(network.getStations());
		assertTrue(!network.getStations().isEmpty());
		Set<ConstraintViolation<Network>> violations = validator.validate(network);
		assertEquals(1, violations.size());
		assertEquals(messages.get("network.starttime.notnull"), violations.iterator().next().getMessage());

		Station abl = network.getStations().get(0);
		assertNotNull(abl);
		assertNotNull(abl.getChannels());
		assertTrue(!abl.getChannels().isEmpty());
		Set<ConstraintViolation<Station>> v = validator.validate(abl);
		assertEquals(0, v.size());

		assertNotNull(abl.getChannels());
		assertTrue(!abl.getChannels().isEmpty());

		Channel ehz1 = abl.getChannels().get(0);
		Channel ehz2 = abl.getChannels().get(1);
		Channel ehz3 = abl.getChannels().get(2);
		Set<ConstraintViolation<Channel>> cv = validator.validate(ehz1);
		assertEquals(1, cv.size());

		Equipment eq = ehz1.getSensor();
		assertNotNull(eq);
		String sensorMessage = (String) messages.get("channel.sensor.description.notnull");

		Set<ConstraintViolation<Equipment>> eqv = validator.validate(eq);
		assertEquals(1, eqv.size());
		assertEquals(sensorMessage, eqv.iterator().next().getMessage());

		Sensitivity is = ehz1.getResponse().getInstrumentSensitivity();
		assertNotNull(is);
		assertNotNull(is.getInputUnits());
		String iu = is.getInputUnits().getName();
		assertNotNull(iu);

	}

	@Test
	public void noSensorDescription_310() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("ANMO_BHZ_nosensor_description_310.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network iu = networks.get(0);
		assertNotNull(iu);
		assertNotNull(iu.getStations());
		assertTrue(!iu.getStations().isEmpty());
		Set<ConstraintViolation<Network>> violations = validator.validate(iu);
		assertEquals(0, violations.size());

		Station station1 = iu.getStations().get(0);
		assertNotNull(station1);
		assertNotNull(station1.getChannels());
		assertTrue(!station1.getChannels().isEmpty());
		Channel channel1 = station1.getChannels().get(0);
		Set<ConstraintViolation<Channel>> cv1 = validator.validate(channel1);
		assertEquals(0, cv1.size());

		Station station2 = iu.getStations().get(1);
		assertNotNull(station2);

		assertNotNull(station2.getChannels());
		assertTrue(!station2.getChannels().isEmpty());
		Channel channel2 = station2.getChannels().get(0);
		Set<ConstraintViolation<Channel>> cv2 = validator.validate(channel2);
		assertEquals(0, cv2.size());

		Set<ConstraintViolation<Equipment>> ev = validator.validate(channel2.getSensor());
		String sensorMessage = (String) messages.get("channel.sensor.notnull");
		assertEquals(messages.get("channel.sensor.description.notnull"), ev.iterator().next().getMessage());

		Station station3 = iu.getStations().get(2);
		assertNotNull(station3.getChannels());
		assertTrue(!station3.getChannels().isEmpty());
		Channel channel3 = station3.getChannels().get(0);
		Set<ConstraintViolation<Channel>> cv3 = validator.validate(channel3);
		assertEquals(1, cv3.size());
		assertEquals(sensorMessage, cv3.iterator().next().getMessage());

	}

	@Test
	public void stageNoUnits402() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("XX-TEST-GainOnlyStage-UnitTestFail.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		List<Network> networks = root.getNetwork();

		Network xx = networks.get(0);
		assertNotNull(xx);
		assertNotNull(xx.getStations());
		assertTrue(!xx.getStations().isEmpty());
		Set<ConstraintViolation<Network>> violations = validator.validate(xx);
		assertEquals(0, violations.size());

		Station xxxx = xx.getStations().get(0);
		assertNotNull(xxxx);
		assertNotNull(xxxx.getChannels());
		assertTrue(!xxxx.getChannels().isEmpty());
		Channel channel1 = xxxx.getChannels().get(0);
		Set<ConstraintViolation<Channel>> cv1 = validator.validate(channel1);
		assertEquals(0, cv1.size());

		Channel bhe = xxxx.getChannels().get(0);
		Set<ConstraintViolation<Channel>> cv = validator.validate(bhe);
		assertEquals(0, cv.size());

		for (ResponseStage stage : bhe.getResponse().getStage()) {
			Set<ConstraintViolation<ResponseStage>> v = validator.validate(stage);
			assertEquals(0, v.size());
		}

	}

}
