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
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.validation.Errors;
import edu.iris.dmc.validation.LEVEL;
import edu.iris.dmc.validation.ValidatorService;
import edu.iris.dmc.validation.ValidatorServiceImp;
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

	@Test
	public void nonZeroSampleRate() throws Exception {
		JAXBContext jaxbContext = (JAXBContext) JAXBContext.newInstance(FDSNStationXML.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		in = this.getClass().getClassLoader().getResourceAsStream("IU_BILL_00_VHZ_resp.xml");
		FDSNStationXML root = (FDSNStationXML) jaxbUnmarshaller.unmarshal(in);

		Errors errors = new Errors();
		service.run(root.getNetwork(), LEVEL.RESPONSE, errors);
		assertTrue(errors.isEmpty());
	}
}
