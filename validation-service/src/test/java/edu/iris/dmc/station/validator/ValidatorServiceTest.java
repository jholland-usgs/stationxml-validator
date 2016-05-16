package edu.iris.dmc.station.validator;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
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
import edu.iris.dmc.fdsn.station.model.LEVEL;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Sensitivity;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.service.Errors;
import edu.iris.dmc.service.ValidatorService;
import edu.iris.dmc.service.ValidatorServiceImp;
import edu.iris.dmc.validation.validator.ResponseGroup;

public class ValidatorServiceTest {

	private static ValidatorService service;
	private InputStream in;
	private static Properties messages = new Properties();

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		service = new ValidatorServiceImp(validator);
		InputStream is = null;
		try {
			is = AllRulesTest.class.getClassLoader().getResourceAsStream("ValidationMessages.properties");
			messages.load(is);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	// @Test
	public void nonZeroSampleRate() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		in = this.getClass().getClassLoader().getResourceAsStream("IU_BILL_00_VHZ_resp.xml");
		FDSNStationXML root = (FDSNStationXML) jaxbUnmarshaller.unmarshal(in);

		Errors errors = service.run(root.getNetwork(), LEVEL.RESPONSE, null);
		for (edu.iris.dmc.service.Error error : errors.getAll()) {
			System.out.println("////" + error.getMessage());
		}
		// assertTrue(errors.isEmpty());
	}

	@Test
	public void noCoeef_411() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller xmlProcessor = jaxbContext.createUnmarshaller();
		in = this.getClass().getClassLoader().getResourceAsStream("411_CI_ABL.xml");
		FDSNStationXML root = (FDSNStationXML) xmlProcessor.unmarshal(in);
		Errors errors = service.run(root.getNetwork(), LEVEL.RESPONSE, null);
		for (edu.iris.dmc.service.Error e : errors.getAll()) {
			System.out.println(
					e.getMessage() + " [" + e.getInvalidValue() + "]" + "   " + e.getPath() + "  " + e.getId());
		}
		/*
		 * Network network = networks.get(0); assertNotNull(network);
		 * assertNotNull(network.getStations());
		 * assertTrue(!network.getStations().isEmpty());
		 * Set<ConstraintViolation<Network>> violations =
		 * validator.validate(network); assertEquals(1, violations.size());
		 * assertEquals(messages.get("network.starttime.notnull"),
		 * violations.iterator().next().getMessage());
		 * 
		 * Station abl = network.getStations().get(0); assertNotNull(abl);
		 * assertNotNull(abl.getChannels());
		 * assertTrue(!abl.getChannels().isEmpty());
		 * Set<ConstraintViolation<Station>> v = validator.validate(abl);
		 * assertEquals(0, v.size());
		 * 
		 * assertNotNull(abl.getChannels());
		 * assertTrue(!abl.getChannels().isEmpty());
		 * 
		 * Channel ehz1 = abl.getChannels().get(0); Channel ehz2 =
		 * abl.getChannels().get(1); Channel ehz3 = abl.getChannels().get(2);
		 * Set<ConstraintViolation<Channel>> cv = validator.validate(ehz1);
		 * assertEquals(1, cv.size());
		 * assertEquals(messages.get("channel.sensor.notnull"),cv.iterator().
		 * next().getMessage());
		 * 
		 * Sensitivity is = ehz1.getResponse().getInstrumentSensitivity();
		 * assertNotNull(is); assertNotNull(is.getInputUnits()); String iu =
		 * is.getInputUnits().getName(); assertNotNull(iu);
		 * 
		 * String iumessage = (String) messages.get("sensitivity.input.unit");
		 * iumessage = iumessage.replace("${validatedValue.name}", iu);
		 * Set<ConstraintViolation<Gain>> gv = validator.validate(is);
		 * assertEquals(1, gv.size());
		 * 
		 * ConstraintViolation<Gain> gvm = gv.iterator().next();
		 * assertEquals(iumessage, gvm.getMessage());
		 * 
		 * assertNotNull(ehz1.getSensor()); Set<ConstraintViolation<Equipment>>
		 * eqViolation = validator.validate(ehz1.getSensor()); assertEquals(1,
		 * eqViolation.size());
		 */
	}

}
