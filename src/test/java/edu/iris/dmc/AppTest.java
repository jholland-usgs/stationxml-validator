package edu.iris.dmc;


/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) throws Exception {
		args = new String[] {"--format","report","--ignore-warnings","--ignore-rules","105","--debug",
				"/Users/Suleiman/validations/channel_300"};
		Application app = new Application();
		app.main(args);
	}
}
