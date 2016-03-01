package edu.iris.dmc.station.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
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
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.Station;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, XmlProcessor.class })
public class StationTest {

	@Autowired
	private XmlProcessor xmlProcessor;

	@Autowired
	private Validator validator;

	@Test
	public void notNullCode() throws Exception {
		Resource resource = new ClassPathResource("ANMO_BHZ.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream());
		List<Network> networks = root.getNetwork();

		Network iu = networks.get(0);
		assertNotNull(iu);
		Station anmo = iu.getStations().get(0);
		assertNotNull(anmo);

		javax.validation.ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		javax.validation.Validator validator = factory.getValidator();
		Set<ConstraintViolation<Station>> violations = validator.validate(anmo);
		for (ConstraintViolation<Station> violation : violations) {
			System.out.println("[Start Tag Line:"+anmo.locator.getLineNumber()+"] "+violation.getMessage());
		}
		assertEquals(0, violations.size());
		anmo.setCode(null);
		violations = validator.validate(anmo);
		assertEquals(1, violations.size());
		// Violation violation = violations.iterator().next();
		// assertTrue(violation.getRule() instanceof NotNull);
		anmo.setCode("ANMO");
	}
	/*
	 * @Test public void notNullElevation() throws Exception { Network iu =
	 * networks.get(0); assertNotNull(iu); Station anmo =
	 * iu.getStations().get(0); assertNotNull(anmo); ValidatorFactory factory =
	 * ServiceFactory.getService(ValidatorFactory.class); Validator validator =
	 * factory.getStationValidator(); Collection<Violation> violations =
	 * validator.run(anmo); assertEquals(0, violations.size()); Distance
	 * distance = anmo.getElevation(); anmo.setElevation(null); violations =
	 * validator.run(anmo); assertEquals(1, violations.size()); Violation
	 * violation = violations.iterator().next(); assertTrue(violation.getRule()
	 * instanceof NotNull); anmo.setElevation(distance); }
	 * 
	 * @Test public void notNullCreationDate() throws Exception { Network iu =
	 * networks.get(0); assertNotNull(iu); Station anmo =
	 * iu.getStations().get(0); assertNotNull(anmo); ValidatorFactory factory =
	 * ServiceFactory.getService(ValidatorFactory.class); Validator validator =
	 * factory.getStationValidator(); Collection<Violation> violations =
	 * validator.run(anmo); assertEquals(0, violations.size());
	 * XMLGregorianCalendar calendar = anmo.getCreationDate();
	 * anmo.setElevation(null); violations = validator.run(anmo);
	 * assertEquals(1, violations.size()); Violation violation =
	 * violations.iterator().next(); assertTrue(violation.getRule() instanceof
	 * NotNull); anmo.setCreationDate(calendar); }
	 * 
	 * @Test public void codeTooLong() throws Exception { Network iu =
	 * networks.get(0); assertNotNull(iu); Station anmo =
	 * iu.getStations().get(0); assertNotNull(anmo); ValidatorFactory factory =
	 * ServiceFactory.getService(ValidatorFactory.class); Validator validator =
	 * factory.getStationValidator(); Collection<Violation> violations =
	 * validator.run(anmo); assertEquals(0, violations.size());
	 * anmo.setCode("ANMO12"); violations = validator.run(anmo); assertEquals(1,
	 * violations.size()); Violation violation = violations.iterator().next();
	 * assertTrue(violation.getRule() instanceof ExpressionRule);
	 * anmo.setCode("ANMO"); }
	 * 
	 * @Test public void codeTooShort() throws Exception { Network iu =
	 * networks.get(0); assertNotNull(iu); Station anmo =
	 * iu.getStations().get(0); assertNotNull(anmo); ValidatorFactory factory =
	 * ServiceFactory.getService(ValidatorFactory.class); Validator validator =
	 * factory.getStationValidator(); Collection<Violation> violations =
	 * validator.run(anmo); assertEquals(0, violations.size());
	 * anmo.setCode(""); violations = validator.run(anmo); assertEquals(1,
	 * violations.size()); Violation violation = violations.iterator().next();
	 * assertTrue(violation.getRule() instanceof ExpressionRule);
	 * anmo.setCode("ANMO"); }
	 */
}
