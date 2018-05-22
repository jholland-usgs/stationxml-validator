package edu.iris.dmc;


/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) throws Exception {
		args = new String[] {"--format","csv","/Users/Suleiman/PROJECTS/StationXML-Validator/src/test/resources/AUMCQBHZ_stageNOSEQUENCE.xml"};
		Application app = new Application();
		app.main(args);
	}
}
