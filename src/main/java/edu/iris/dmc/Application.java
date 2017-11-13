package edu.iris.dmc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.beust.jcommander.JCommander;

import edu.iris.dmc.fdsn.station.model.BaseNodeType.LEVEL;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.station.RuleEngineService;
import edu.iris.dmc.station.actions.Action;
import edu.iris.dmc.station.io.CsvPrintStream;
import edu.iris.dmc.station.io.HtmlPrintStream;
import edu.iris.dmc.station.io.RuleResultPrintStream;
import edu.iris.dmc.station.io.XmlPrintStream;
import edu.iris.dmc.station.rules.Result;
import edu.iris.dmc.station.rules.Rule;
import edu.iris.dmc.station.rules.RuleContext;
import edu.iris.dmc.station.rules.UnitTable;

public class Application {
	private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

	private static Args args = new Args();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] argv) throws Exception {

		
		JCommander.newBuilder().addObject(args).build().parse(argv);
		if (args.version) {
			System.out.println(Application.getVersion());
			System.exit(0);
		} else if (args.help) {
			help();
			System.exit(0);
		} else if (args.printRules) {
			printRules();
			System.exit(0);
		} else if (args.printUnits) {
			printUnits();
			System.exit(0);
		} else if (args.input == null) {
			System.out.println("File is required!");
			help();
			System.exit(1);
		}

		Application app = new Application();
		app.run();
	}

	public void run() throws Exception {
		final Level logLevel = args.debug ? Level.FINER : Level.INFO;
		Handler consoleHandler = new ConsoleHandler();
		LOGGER.setLevel(logLevel);
		LOGGER.addHandler(consoleHandler);

		LEVEL level = LEVEL.parse(args.level.toLowerCase());
		OutputStream out = System.out;

		if (args.output != null) {
			out = new FileOutputStream(new File(args.output));
			// Handler fileHandler = new FileHandler(outputFile[1]);
			// LOGGER.addHandler(fileHandler);
		}

		List<Integer> ignoreList = new ArrayList<Integer>();

		List<String> input = new ArrayList<>();
		// PrintStream stream = new PrintStream(out);

		RuleContext rulesContext = RuleContext.of(LEVEL.RESPONSE);
		int EXIT = 0;
		if (args.input.startsWith("http://")) {
			input.add(args.input);
		} else {
			File file = new File(args.input);
			if (!file.exists()) {
				System.out.println("File does not exist.  File is required!");
				help();
				System.exit(1);
			}

			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					input.add(f.getAbsolutePath());
				}
			} else {
				input.add(file.getAbsolutePath());
			}
		}
		run(rulesContext, input, args.format, out, level, ignoreList);
		if (out != null) {
			out.close();
		}
		
	
		System.exit(EXIT);
	}

	private void run(RuleContext context, List<String> input, String format, OutputStream outputStream, LEVEL level,
			List<Integer> ignoreList) {

		RuleEngineService ruleEngineService = new RuleEngineService();
		try (final RuleResultPrintStream ps = getOutputStream(format, outputStream)) {
			ps.printHeader();
			InputStream is = null;
			Bool bool = new Bool();
			for (String uri : input) {
				if (uri.startsWith("http://")) {
					is = new URL(uri).openStream();
				} else {
					if (!uri.endsWith(".xml")) {
						continue;
					}
					File file = new File(uri);
					if (!file.exists()) {
						System.err.println("File does not exist.  File is required!");
						help();
						System.exit(1);
					}
					is = new FileInputStream(new File(uri));
				}

				FDSNStationXML document = (FDSNStationXML) theMarshaller().unmarshal(new StreamSource(is));

				ruleEngineService.executeAllRules(document, context, new Action() {
					@Override
					public void update(RuleContext context, Result result) {
						try {
							bool.value = false;
							ps.print(uri, result);
							ps.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
					}
				}
			}
			if (bool.value) {
				ps.printMessage("PASSED");
			}
			ps.printFooter();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch(UnmarshalException e){
			System.err.println(e.getCause().getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private RuleResultPrintStream getOutputStream(String format, OutputStream outputStream) throws IOException {
		if (format == null || format.isEmpty() || "html".equalsIgnoreCase(format)) {
			return new HtmlPrintStream(outputStream);
		} else if ("csv".equalsIgnoreCase(format)) {
			return new CsvPrintStream(outputStream);
		} else if ("xml".equalsIgnoreCase(format)) {
			return new XmlPrintStream(outputStream);
		} else {
			throw new IOException("Invalid format [" + format + "] requested");
		}
	}

	private Unmarshaller theMarshaller() throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(edu.iris.dmc.fdsn.station.model.ObjectFactory.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			StreamSource stream = new StreamSource(Application.class.getClassLoader().getResourceAsStream("fdsn-station-1.0.xsd"));
			SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(stream);
			u.setSchema(schema);
			return u;
		} catch (JAXBException | SAXException e) {
			throw new IOException(e);
		}

	}

	private static String getVersion() throws IOException {
		Properties prop = new Properties();
		InputStream in = Application.class.getClassLoader().getResourceAsStream("application.properties");
		prop.load(in);
		in.close();
		return prop.getProperty("application.version");
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

	private static void printRules() {

		RuleEngineService ruleEngineService = new RuleEngineService();
		for (Rule rule : ruleEngineService.getRules()) {
			System.out.printf("%-8s %s\n", rule.getId(), rule.getDescription());
		}
	}

	private static void printUnits() {
		System.out.println("UNIT TABLE:");
		System.out.println("-------------------------------------");
		for (String unit : UnitTable.units) {
			System.out.println(unit);
		}

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
		System.out.println("   --rules 			: print a list of validation rules");
		System.out.println("   --units 			: print a list of units used to validate");
		System.out.println("   --format 	    : csv|html|xml");
		System.out.println("   --summary     	: print summary only report for errors if any");
		System.out.println("   --debug       	:");
		System.out.println("   --help        	: print this message");
		System.out.println("===============================================================");
		System.exit(0);
	}

	class Bool {
		boolean value = true;
	}
}
