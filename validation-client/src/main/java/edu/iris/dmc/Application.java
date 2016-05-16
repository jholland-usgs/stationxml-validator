package edu.iris.dmc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.oxm.UnmarshallingFailureException;
import org.xml.sax.SAXParseException;

import edu.iris.dmc.Table.ALIGN;
import edu.iris.dmc.fdsn.station.model.LEVEL;
import edu.iris.dmc.service.Errors;

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
		List<String> list = new ArrayList<String>();
		for (String arg : args) {
			if (arg.equals("--version")) {
				System.out.println(VERSION);
				System.exit(0);
			} else if (arg.equals("--help")) {
				help();
				System.exit(0);
			} else if (arg.equals("--print-rules")) {
				printRules();
				System.exit(0);
			} else {
				list.add(arg);
			}
		}

		args = new String[list.size()];
		list.toArray(args);
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
		app.run(args);
	}

	private static void printRules() {
		try (InputStream is = Application.class.getClassLoader().getResourceAsStream("ValidationMessages.properties");
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			System.out.println(header("IRIS: Validation rules", 70));
			String l = null;
			System.out.println(String.format("%10s %10s", "Number", "Description"));

			Table table = new Table(70);
			int row = 0;
			while ((l = br.readLine()) != null) {
				if (l.trim().isEmpty()) {
					continue;
				}
				String[] array = l.split("=");
				String[] rules = array[1].split(",");
				table.add(row, 0, rules[0].trim(), ALIGN.RIGHT, 10);
				table.add(row, 1, rules[1].trim(), ALIGN.LEFT, 60);
				row++;
			}
			System.out.println(table.print());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String header(String s, int width) {
		int l = s.length();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < width; i++) {
			builder.append("=");
		}
		builder.append(System.getProperty("line.separator"));
		for (int i = 0; i < (width - l) / 2; i++) {
			builder.append(" ");
		}
		builder.append(s);
		for (int i = 0; i < (width - l) / 2; i++) {
			builder.append(" ");
		}
		builder.append(System.getProperty("line.separator"));
		for (int i = 0; i < width; i++) {
			builder.append("=");
		}
		return builder.toString();
	}

	@Override
	public void run(String... args) throws Exception {

		LEVEL level = LEVEL.RESPONSE;
		boolean debug = false;
		String filename = null;
		OutputStream out = System.out;
		PrintStream stream = null;

		List<Integer> ignoreList = new ArrayList<Integer>();
		List<Integer> availableRules = new ArrayList<Integer>();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("--level")) {
				String[] array = arg.split("=");
				if (array.length == 2) {
					level = LEVEL.parse(array[1].toLowerCase());
				}
			} else if (arg.equals("--debug")) {
				debug = true;
			} else if (arg.equals("--ignore-rules")) {
				i = i + 1;
				String ignore = args[i];
				ignore = ignore.trim();
				if (!ignore.isEmpty()) {
					String array[] = ignore.split(",");
					InputStream is = getClass().getResourceAsStream("ValidationMessages.properties");

					if (is != null) {
						try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
							String line;
							while ((line = br.readLine()) != null) {
								String[] property = line.split("=");
								String[] items = property[1].split(",");
								try {
									availableRules.add(Integer.valueOf(items[0]));
								} catch (NumberFormatException e) {

								}
							}
						}
					}
					for (String s : array) {
						try {
							int number = Integer.valueOf(s);
							if (availableRules.contains(number)) {
								ignoreList.add(number);
							} else {
								System.out.println("Rule [" + number + "] is not available!");
								help();
								System.exit(0);
							}
						} catch (NumberFormatException e) {
							System.out.println("--ignore-rules: invalid rule number " + s);
							System.out.println(e.getMessage());
							help();
						}
					}
				}
			} else {
				// assume the file name
				filename = arg;
			}
		}

		if (filename == null) {
			System.out.println("File is required!");
			help();
			System.exit(0);
		}

		File file = new File(filename);
		if (!file.exists()) {
			System.out.println("File does not exist.  File is required!");
			help();
			System.exit(0);
		}

		File[] list = null;
		int size = 0;
		if (file.isDirectory()) {
			list = file.listFiles();
			size = list.length;
		} else {
			list = new File[] { file };
		}

		for (File f : list) {
			try (InputStream is = new FileInputStream(f)) {
				Errors errors = controller.run(is, level, ignoreList);
				stream = new PrintStream(out);
				if (!errors.isEmpty()) {
					PrintErrorService printer = new PrintErrorService(stream, ",");
					printer.header();
					for (edu.iris.dmc.service.Error error : errors.getAll()) {
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
	}

	private void printInfo(SAXParseException e) {
		System.out.println(" Line number: " + e.getLineNumber() + " Column number: " + e.getColumnNumber()
				+ " Message: " + e.getMessage());
	}

	private static void help() {
		String blanks = "";
		for (int i = VERSION.length(); i < 35; i++) {
			blanks += " ";
		}
		System.out.println("===============================================================");
		System.out.println("|                   FDSN StationXml validator                  |");
		System.out.println("|                  Version " + VERSION + blanks + "|");
		System.out.println("================================================================");
		System.out.println("Usage:");
		System.out.println("java -jar stationxml-validator [OPTIONS] [FILE]");
		System.out.println("OPTIONS");
		System.out.println("   --[net|sta|cha|resp] default is resp ");
		System.out.println("   --ignore-rules: comma seperated numbers of validation rules");
		System.out.println("   --print-rules : print a list of validation rules");
		System.out.println("   --debug:");
		System.out.println("   --help: print this message");
		System.out.println("+==============================================================");
		System.exit(0);
	}
}
