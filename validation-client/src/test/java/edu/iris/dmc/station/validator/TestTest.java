package edu.iris.dmc.station.validator;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

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
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.validator.ResponseGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, XmlProcessor.class })
public class TestTest {

	@Autowired
	private XmlProcessor xmlProcessor;

	@Autowired
	private Validator validator;

	@Test
	public void hello() throws Exception {
	}

	// @Test
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
			for (ConstraintViolation<Channel> v : channelViolations) {
				System.out.println(v.getMessage());
			}
			assertEquals(0, channelViolations.size());

		}
	}

	@Test
	public void shouldPass402() throws Exception {
		Resource resource = new ClassPathResource("402_pass.xml");
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

		Set<ConstraintViolation<Channel>> channelViolations = validator.validate(channel, ResponseGroup.class);
		assertEquals(0, channelViolations.size());

		assertNotNull(channel.getResponse());
		assertNotNull(channel.getResponse().getStage());
		assertEquals(6, channel.getResponse().getStage().size());
		ResponseStage stage = channel.getResponse().getStage().get(2);
		Set<ConstraintViolation<Response>> responseViolations = validator.validate(channel.getResponse());
		assertEquals(0, responseViolations.size());

	}
}
