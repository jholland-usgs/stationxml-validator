package edu.iris.dmc.station.validator;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.iris.dmc.AppConfig;
import edu.iris.dmc.XmlProcessor;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Response;
import edu.iris.dmc.fdsn.station.model.Station;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, XmlProcessor.class })
public class AllRulesTest {

	@Autowired
	private XmlProcessor xmlProcessor;

	@Autowired
	private Validator validator;

	@Test
	public void networkCodeTooLong() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_lengthNETCODE.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);

		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(1, violations.size());

		ConstraintViolation<Network> violation = violations.iterator().next();
		assertEquals("IIII", violation.getInvalidValue());
		assertEquals("[SEED:b50,16] IIII doesn't match $[A-Za-z0-9\\*\\?]{1,2}", violation.getMessage());
	}

	@Test
	public void networkCodeWrong() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_charNETCODE.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);

		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(1, violations.size());

		ConstraintViolation<Network> violation = violations.iterator().next();
		assertEquals("#$", violation.getInvalidValue());
		assertEquals("[SEED:b50,16] #$ doesn't match $[A-Za-z0-9\\*\\?]{1,2}", violation.getMessage());
	}

	@Test
	public void networkInvalidStartTime() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_badNetSTARTTIME.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
		List<Network> networks = root.getNetwork();

		Network n = networks.get(0);
		assertNotNull(n);

		Set<ConstraintViolation<Network>> violations = validator.validate(n);
		assertEquals(1, violations.size());

		ConstraintViolation<Network> violation = violations.iterator().next();
		assertEquals("startDate should be before endDate", violation.getMessage());
	}

	@Test
	public void nullStationCode() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_nullSTACODE.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
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
		assertEquals("[SEED:b50,3] [] doesn't match $[A-Za-z0-9\\*\\?]{1,5}", violation.getMessage());

		station.setCode("ANMO");
		stationViolations = validator.validate(station);
		assertEquals(0, stationViolations.size());
	}

	@Test
	public void nullChannelCode() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_nullCHACODE.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
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
			for(ConstraintViolation<Channel> v:channelViolations){
				System.out.println(":::::::"+v.getMessage());
				System.out.println(":::::::"+channel.getAzimuth().getValue()+"    "+channel.getCode());
			}
			assertEquals(1, channelViolations.size());
			ConstraintViolation<Channel> violation = channelViolations.iterator().next();
			assertEquals("[SEED:b52,4] [] doesn't match $[A-Za-z0-9\\*\\?]{1,3}", violation.getMessage());
			channel.setCode("BHZ");
			channelViolations = validator.validate(channel);
			assertEquals(0, channelViolations.size());
		}
	}

	@Test
	public void nulllocationCode() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_nullLOCCODE.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
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
			for(ConstraintViolation<Channel> v:channelViolations){
				System.out.println(v.getMessage());
			}
			assertEquals(0, channelViolations.size());
			
		}
	}

	@Test
	public void channelTooFarFromStation() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_CHAtooFarFromSTA.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
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
		Resource resource = new ClassPathResource("AUMCQBHZ_stageNOSEQUENCE.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
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
		ConstraintViolation<Response> violation = responseViolations.iterator().next();
		assertEquals("Stage number attribute must start at 1, be present in numerical order and have no gaps", violation.getMessage());
		
	}

	@Test
	public void nonZeroSampleRate() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_emptyINSTSENSITIVITY.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
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
		Resource resource = new ClassPathResource("IIKDAK10VHZ_invalidCHAAZ.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
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
		//System.out.println("??????"+channelViolations.iterator().next().getMessage());
		assertEquals(1, channelViolations.size());

	
		
	}
	
}
