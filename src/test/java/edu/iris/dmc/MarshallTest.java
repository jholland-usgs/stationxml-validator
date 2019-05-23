package edu.iris.dmc;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import edu.iris.dmc.station.RuleEngineServiceTest;

public class MarshallTest {

	@Test
	public void umarshall() throws Exception{
		try (InputStream is = RuleEngineServiceTest.class.getClassLoader().getResourceAsStream("F1_101.xml")) {
			DocumentMarshaller.unmarshaller().unmarshal(is);
		}
	}
}
