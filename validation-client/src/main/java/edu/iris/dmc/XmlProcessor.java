package edu.iris.dmc;

import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import edu.iris.dmc.fdsn.station.model.FDSNStationXML;

@Component
public class XmlProcessor {

	@Autowired
	private Jaxb2Marshaller theMarshaller;

	public FDSNStationXML unmarshal(InputStream inputStream) {
		return (FDSNStationXML) theMarshaller.unmarshal(new StreamSource(inputStream));
	}

	public FDSNStationXML unmarshal(InputStream inputStream, Resource schema) throws Exception {
		// marshaller.setSchema(schema);
		return (FDSNStationXML) theMarshaller.unmarshal(new StreamSource(inputStream));
	}
}
