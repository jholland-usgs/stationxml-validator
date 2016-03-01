package edu.iris.dmc.station.validator;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, XmlProcessor.class })
public class XmlValidationTest {

	@Autowired
	private XmlProcessor xmlProcessor;

	@Autowired
	private Resource schemaResource;

	@Test
	public void notNullCode() throws Exception {
		Resource resource = new ClassPathResource("IIKDAK10VHZ_invalidCHAAZ.xml");
		FDSNStationXML root = xmlProcessor.unmarshal(resource.getInputStream(), schemaResource);
		
	}
}
