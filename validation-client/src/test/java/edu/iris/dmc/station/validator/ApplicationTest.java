package edu.iris.dmc.station.validator;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.OutputCapture;

import edu.iris.dmc.Application;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { AppConfig.class, XmlProcessor.class })
public class ApplicationTest {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();



	@Test
	public void notNullCode() throws Exception {
		Application.main(new String[]{"IIKDAK10VHZ_invalidCHAAZ.xml","--debug", "--valid"});
		String output = this.outputCapture.toString();
		System.out.println("output:"+output);
		assertTrue("Wrong output: " + output, output.contains("hel--------p"));
	}
}
