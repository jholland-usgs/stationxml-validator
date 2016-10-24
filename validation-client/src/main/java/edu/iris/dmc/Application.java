package edu.iris.dmc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.oxm.UnmarshallingFailureException;
import org.xml.sax.SAXParseException;

import edu.iris.dmc.fdsn.station.model.LEVEL;
import edu.iris.dmc.print.CSVPrintHandler;
import edu.iris.dmc.printer.PrintErrorService;
import edu.iris.dmc.printer.PrintErrorServiceImp;
import edu.iris.dmc.printer.PrintHandler;
import edu.iris.dmc.service.Errors;
import edu.iris.dmc.validation.rule.Rule;
import edu.iris.dmc.validation.rule.UnitTable;
import edu.iris.dms.table.Column;
import edu.iris.dms.table.Table;
import edu.iris.dms.table.view.ALIGN;
import edu.iris.dms.table.view.Renderer;
import edu.iris.dms.table.view.console.ConsoleTableRenderer;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

	@Autowired
	private ValidStationController controller;

	private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		List<String> list = new ArrayList<String>();
		for (String arg : args) {
			if (arg.equals("--version")) {
				System.out.println(Application.getVersion());
				System.exit(0);
			} else if (arg.equals("--help")) {
				help();
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

	@Override
	public void run(String... args) throws Exception {

		LEVEL level = LEVEL.RESPONSE;
		boolean debug = false;
		String filename = null;
		OutputStream out = System.out;
		PrintStream stream = null;
		boolean summary = false;
		boolean ignoreWarnings = false;

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
				LOGGER.setLevel(Level.INFO);
				Handler consoleHandler = new ConsoleHandler();
				LOGGER.addHandler(consoleHandler);
			} else if (arg.equals("--summary")) {
				summary = true;
			} else if (arg.equals("--print-rules")) {
				printRules();
				System.exit(0);
			} else if (arg.equals("--print-units")) {
				printUnits();
				System.exit(0);
			} else if (arg.startsWith("--output") || arg.startsWith("-o")) {
				String[] outputFile = arg.split("=");
				out = new FileOutputStream(new File(outputFile[1]));
				Handler fileHandler = new FileHandler(outputFile[1]);
				LOGGER.addHandler(fileHandler);

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
			} else if (arg.equals("--ignore-warnings")) {
				ignoreWarnings = true;
			} else {
				// assume the file name
				filename = arg;
			}
		}

		if (filename == null) {
			System.out.println("File is required!");
			help();
			System.exit(1);
		}
		Errors errors = null;
		stream = new PrintStream(out);
		PrintHandler printHandler = new CSVPrintHandler(stream, "|", summary,ignoreWarnings);
		PrintErrorServiceImp printer = new PrintErrorServiceImp();
		printer.setPrintHandler(printHandler);
		int EXIT = 0;
		if (filename.startsWith("http://")) {
			try (InputStream is = new URL(filename).openStream()) {
				LOGGER.info("Validating: " + filename);
				errors = validate(is, level, ignoreList);
				if (!errors.isEmpty()) {
					printer.header();
					print(errors, printer, filename);
					EXIT = 1;
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.err.println(ioe.getMessage());
			} catch (UnmarshallingFailureException e) {
				if (e.getRootCause() instanceof SAXParseException) {
					printInfo((SAXParseException) e.getRootCause());
				} else {
					e.printStackTrace();
				}
				System.err.println(e.getMessage());
			}
		} else {
			File file = new File(filename);
			if (!file.exists()) {
				System.out.println("File does not exist.  File is required!");
				help();
				System.exit(1);
			}

			File[] list = null;
			if (file.isDirectory()) {
				list = file.listFiles();
			} else {
				list = new File[] { file };
			}
			boolean printHeader = true;
			for (File f : list) {
				try (InputStream is = new FileInputStream(f)) {
					errors = validate(is, level, ignoreList);
					if (!errors.isEmpty()) {
						if (printHeader) {
							printHeader = false;
							printer.header();
						}
						print(errors, printer, f.getName());
						EXIT = 1;
					}
				} catch (IOException ioe) {
					EXIT = 1;
					ioe.printStackTrace();
					System.err.println(ioe.getMessage());
				} catch (UnmarshallingFailureException e) {
					EXIT = 1;
					if (e.getRootCause() instanceof SAXParseException) {
						printInfo((SAXParseException) e.getRootCause());
					} else {
						e.printStackTrace();
					}
					System.err.println(e.getMessage());
				}
			}
		}
		if (EXIT == 0) {
			printer.getPrintHandler().print("Validation successful, all tests passed");
		}

		if (out != null) {
			out.close();
		}
		System.exit(EXIT);
	}

	private Errors validate(InputStream is, LEVEL level, List<Integer> ignoreList) {
		Errors errors = controller.run(is, level, ignoreList);
		return errors;
	}

	private void print(Errors errors, PrintErrorService printer, String resource) {
		for (edu.iris.dmc.service.Error error : errors.getAll()) {
			printer.print(error, resource);
		}
		printer.flush();
	}

	private void printRules() {
		Table table = new Table("IRIS: Validation rules");
		table.setBorder(1);
		Column id = new Column("id", 10);
		Column description = new Column("description", 60);
		table.addAll(id, description);

		List<Rule> list = controller.getRules();
		for (int i = 0; i < list.size(); i++) {
			Rule rule = list.get(i);
			table.add(i, 0, "" + rule.getId(), ALIGN.RIGHT, 1);
			table.add(i, 1, rule.getMessage(), ALIGN.LEFT, 1);
		}
		Renderer<Table> renderer = new ConsoleTableRenderer(System.out);
		renderer.render(table);
	}

	private void printUnits() {

		UnitTable table = controller.getUnits();
		// Collections.sort(table.getUnits());
		System.out.println("UNIT TABLE, verified:" + table.getTag());
		System.out.println("-------------------------------------");
		int row = 0;
		for (String unit : table.getUnits()) {
			System.out.println(unit);
			row++;
		}

	}

	private static String getVersion() throws IOException {
		Properties prop = new Properties();
		InputStream in = Application.class.getClassLoader().getResourceAsStream("application.properties");
		prop.load(in);
		in.close();
		return prop.getProperty("application.version");
	}

	private void printInfo(SAXParseException e) {
		System.out.println(" Line number: " + e.getLineNumber() + " Column number: " + e.getColumnNumber()
				+ " Message: " + e.getMessage());
	}

	private static String center(String text, int length, String pad) {
		int width = length - text.length();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < width / 2; i++) {
			builder.append(pad);
		}
		builder.append(text);
		int remainder = length - builder.length();
		for (int i = 0; i < remainder; i++) {
			builder.append(pad);
		}
		return builder.toString();
	}

	private static void help() throws IOException {
		String version = "Version " + getVersion();
		version = center(version, 62, " ");

		System.out.println("===============================================================");
		System.out.println("|" + center("FDSN StationXml validator", 62, " ") + "|");
		System.out.println("|" + version + "|");
		System.out.println("================================================================");
		System.out.println("Usage:");
		System.out.println("java -jar stationxml-validator [OPTIONS] [FILE]");
		System.out.println("OPTIONS");
		System.out.println("   --[net|sta|cha|resp] default is resp ");
		System.out.println("   --output      	: where to output result, default is System.out");
		System.out.println("   --ignore-warnings: don't show warnings");
		System.out.println("   --ignore-rules	: comma seperated numbers of validation rules");
		System.out.println("   --print-rules 	: print a list of validation rules");
		System.out.println("   --print-units 	: print a list of units used to validate");
		System.out.println("   --summary     	: print summary only report for errors if any");
		System.out.println("   --debug       	:");
		System.out.println("   --help        	: print this message");
		System.out.println("===============================================================");
		System.exit(0);
	}
}
