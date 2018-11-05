package edu.iris.dmc;


/**
 * Unit test for simple App.
 */
public class AppTest {
	public static void main(String[] args) throws Exception {
		args = new String[] {"--format","report","--ignore-warnings","--ignore-rules","105",
				"/Users/Suleiman/AU.MILA.dataless.fromHughGlanville.20181018"};
		Application app = new Application();
		app.main(args);
	}
}
