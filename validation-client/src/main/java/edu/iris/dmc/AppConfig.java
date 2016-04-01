package edu.iris.dmc;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import edu.iris.dmc.validation.ValidatorService;
import edu.iris.dmc.validation.ValidatorServiceImp;

@Configuration
public class AppConfig {

	@Bean
	public javax.validation.Validator validator() {
		//Validation.buildDefaultValidatorFactory().getValidator();
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jaxb.formatted.output", true);
		theMarshaller.setMarshallerProperties(map);
		theMarshaller.afterPropertiesSet();
		return theMarshaller;

	}

}
