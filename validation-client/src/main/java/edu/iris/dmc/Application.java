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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	private static String VERSION = "1.0";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[] { "--print-rules" };
		List<String> list = new ArrayList<String>();
		for (String arg : args) {
			if (arg.equals("--version")) {
				System.out.println(VERSION);
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
		if (file.isDirectory()) {
			list = file.listFiles();
		} else {
			list = new File[] { file };
		}

		stream = new PrintStream(out);
		PrintHandler printHandler = new CSVPrintHandler(stream, "|", summary);
		PrintErrorService printer = new PrintErrorServiceImp();
		printer.setPrintHandler(printHandler);
		printer.header();
		for (File f : list) {
			try (InputStream is = new FileInputStream(f)) {
				Errors errors = controller.run(is, level, ignoreList);
				if (!errors.isEmpty()) {
					for (edu.iris.dmc.service.Error error : errors.getAll()) {
						printer.print(error, f.getName());
					}
				} else {
					// System.out.println("No errors");
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
			stream.flush();
		}
		if (out != null) {
			out.close();
		}

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
		Table table = new Table("IRIS: Validation units");
		table.setBorder(1);
		Column id = new Column("unit", 100);
		table.addAll(id);

		Set<String> units = controller.getUnits();
		int row = 0;
		for (String unit : units) {
			table.add(row, 0, unit, ALIGN.LEFT);
			row++;
		}
		Renderer<Table> renderer = new ConsoleTableRenderer(System.out);
		renderer.render(table);
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
		System.out.println("   --print-units : print a list of units used to validate");
		System.out.println("   --summary     : print summary only report for errors if any");
		System.out.println("   --debug:");
		System.out.println("   --help: print this message");
		System.out.println("+==============================================================");
		System.exit(0);
	}
}
