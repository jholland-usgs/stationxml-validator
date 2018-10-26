package edu.iris.dmc;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import edu.iris.dmc.fdsn.station.model.FDSNStationXML;


public class DocumentMarshaller {

	public static FDSNStationXML unmarshal(InputStream inputStream) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(edu.iris.dmc.fdsn.station.model.ObjectFactory.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (FDSNStationXML) jaxbUnmarshaller.unmarshal(inputStream);
	}
	
	public static FDSNStationXML unmarshalString(String inputStream) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(edu.iris.dmc.fdsn.station.model.ObjectFactory.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (FDSNStationXML) jaxbUnmarshaller.unmarshal(new StringReader(inputStream));
	}
}
