package edu.iris.dmc;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import edu.iris.dmc.fdsn.station.model.FDSNStationXML;

public class DocumentMarshaller {

	public static FDSNStationXML unmarshal(InputStream is) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(edu.iris.dmc.fdsn.station.model.ObjectFactory.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (FDSNStationXML) jaxbUnmarshaller.unmarshal(new StreamSource(is));
	}
}
