package edu.iris.dmc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.oxm.UnmarshallingFailureException;
import org.xml.sax.SAXParseException;

import edu.iris.dmc.validation.Errors;
import edu.iris.dmc.validation.LEVEL;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

	@Autowired
	private ValidStationController controller;

	private static String VERSION = "1.0";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		String VERSION = "1.0";
		LEVEL level = LEVEL.RESPONSE;
		boolean debug = false;
		String filename = null;
		OutputStream out = System.out;
		PrintStream stream = null;

		for (String arg : args) {
			if (arg.startsWith("--level")) {
				String[] array = arg.split("=");
				if (array.length == 2) {
					level = LEVEL.parse(array[1].toLowerCase());
				}
			} else if (arg.equals("--debug")) {
				debug = true;
			} else if (arg.equals("--version")) {
				System.out.println(VERSION);
				System.exit(0);
			} else if (arg.equals("--help")) {
				help();
				System.exit(0);
			} else {
				// assume the file name
				filename = arg;
			}
		}

		if (filename == null) {
			System.out.println("File required!");
			help();
			System.exit(0);
		}

		try (InputStream is = new FileInputStream(new File(filename))) {
			Errors errors = controller.run(is, level);
			stream = new PrintStream(out);
			if (!errors.isEmpty()) {
				PrintErrorService printer = new PrintErrorService(stream, ",");
				printer.header();
				for (edu.iris.dmc.validation.Error error : errors.getAll()) {
					printer.print(error);
				}
			} else {
				System.out.println("No errors");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnmarshallingFailureException e) {
			if (e.getRootCause() instanceof SAXParseException) {
				printInfo((SAXParseException) e.getRootCause());
			} else {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printInfo(SAXParseException e) {
		System.out.println(" Line number: " + e.getLineNumber() + " Column number: " + e.getColumnNumber()
				+ " Message: " + e.getMessage());
	}

	private void help() {
		System.out.println("java -jar stationxml-validator [OPTIONS] [FILE]");
		System.out.println("OPTIONS");
		System.out.println("   --[net|sta|cha|resp] default is resp ");
		System.out.println("   --debug:");
		System.out.println("   --help: print this message");
	}

}
