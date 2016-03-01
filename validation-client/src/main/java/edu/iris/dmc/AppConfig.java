package edu.iris.dmc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.xml.sax.SAXException;

import edu.iris.dmc.validation.ValidatorService;
import edu.iris.dmc.validation.ValidatorServiceImp;

@Configuration
public class AppConfig {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setPackagesToScan("edu.iris.dmc.fdsn.station.model");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jaxb.formatted.output", true);
		jaxb2Marshaller.setMarshallerProperties(map);
		return jaxb2Marshaller;
	}

	@Bean
	public javax.validation.Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public ValidatorService validatorService() {
		return new ValidatorServiceImp(validator());
	}

	@Bean
	public Resource schemaResource() {
		return new ClassPathResource("fdsn-station-1.0.xsd");
	}

	@Bean

	public Jaxb2Marshaller theMarshaller() throws Exception {
		Jaxb2Marshaller theMarshaller = new Jaxb2Marshaller();
		theMarshaller.setContextPath("edu.iris.dmc.fdsn.station.model");
		theMarshaller.setSchema(schemaResource());
		theMarshaller.afterPropertiesSet();
		return theMarshaller;

	}

}
