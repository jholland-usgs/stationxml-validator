package edu.iris.dmc;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import edu.iris.dmc.fdsn.station.model.StationxmlException;
import edu.iris.dmc.seed.SeedException;
import edu.iris.dmc.station.RuleEngineServiceTest;

public class MarshallTest {

	@Test
	public void umarshall() throws Exception{
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_101.xml")) {
			DocumentMarshaller.unmarshaller().unmarshal(is);
		}
	}


//@Test
public void xmlxsdfailure() throws Exception{
	try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("xmlVSxsd.xml")) {
	    try {	
		    DocumentMarshaller.unmarshaller().unmarshal(is);
	    }catch(javax.xml.bind.UnmarshalException e) {
			//System.out.println("XML XSD Validation ERROR!", e);
			e.printStackTrace();
	    	throw new StationxmlException(String.format("XML XSD Validation ERROR!"), e);
	}
	}
}
}
