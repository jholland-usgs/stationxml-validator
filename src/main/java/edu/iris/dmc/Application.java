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
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.beust.jcommander.JCommander;

import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.seed.Blockette;
import edu.iris.dmc.seed.Volume;
import edu.iris.dmc.seed.blockette.util.BlocketteItrator;
import edu.iris.dmc.seed.builder.BlocketteBuilder;
import edu.iris.dmc.seed.director.BlocketteDirector;
import edu.iris.dmc.station.RuleEngineService;
import edu.iris.dmc.station.actions.Action;
import edu.iris.dmc.station.converter.SeedToXmlDocumentConverter;
import edu.iris.dmc.station.io.CsvPrintStream;
import edu.iris.dmc.station.io.HtmlPrintStream;
import edu.iris.dmc.station.io.ReportPrintStream;
import edu.iris.dmc.station.io.RuleResultPrintStream;
import edu.iris.dmc.station.io.XmlPrintStream;
import edu.iris.dmc.station.rules.Message;
import edu.iris.dmc.station.rules.Rule;
import edu.iris.dmc.station.rules.RuleContext;
import edu.iris.dmc.station.rules.UnitTable;
import edu.iris.dmc.station.util.SeedUtils;

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
		//final Level logLevel = args.debug ? Level.FINER : Level.INFO;
		//Handler consoleHandler = new ConsoleHandler();
		//LOGGER.setLevel(logLevel);
		//LOGGER.addHandler(consoleHandler);

		OutputStream out = System.out;

		if (args.output != null) {
			out = new FileOutputStream(new File(args.output));
			// Handler fileHandler = new FileHandler(outputFile[1]);
			// LOGGER.addHandler(fileHandler);
		}

		List<String> input = new ArrayList<>();
		// PrintStream stream = new PrintStream(out);
		RuleContext rulesContext = RuleContext.of(args.ignoreWarnings);
		int EXIT = 0;
		if (args.ignoreRules != null) {
			String[] array = args.ignoreRules.split(",");
			for (String a : array) {
				if ("".equals(a.trim())) {
					continue;
				}
				try {
					rulesContext.ignoreRule(Integer.valueOf(a));
				} catch (NumberFormatException e) {
					System.out.println("Invalid value for --ignore-rules: " + a);
					help();
					System.exit(1);
				}
			}
		}
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
					if (!f.getName().startsWith("."))
						input.add(f.getAbsolutePath());
				}
			} else {
				input.add(file.getAbsolutePath());
			}
		}
		run(rulesContext, input, args.format, out);
		if (out != null) {
			out.close();
		}

		System.exit(EXIT);
	}

	private void run(RuleContext context, List<String> input, String format, OutputStream outputStream) {

		RuleEngineService ruleEngineService = new RuleEngineService(context.getIgnoreRules());
		String source = null;
		try (final RuleResultPrintStream ps = getOutputStream(format, outputStream)) {

			InputStream is = null;
			for (String uri : input) {
				source = uri;
				FDSNStationXML document = null;
				if (uri.startsWith("http://")) {
					is = new URL(uri).openStream();
					document = (FDSNStationXML) theMarshaller().unmarshal(new StreamSource(is));
				} else {
					File file = new File(uri);
					if (!file.exists()) {
						System.err.println("File does not exist.  File is required!");
						help();
						System.exit(1);
					}

					if (file.isDirectory()) {
						List<String> list = new ArrayList<>();
						for (File f : file.listFiles()) {
							if (!f.getName().startsWith("."))
								list.add(f.getAbsolutePath());
						}
						run(context, list, format, outputStream);
						continue;
					}
					is = new FileInputStream(new File(uri));
					if (uri.endsWith(".xml")) {
						document = (FDSNStationXML) theMarshaller().unmarshal(new StreamSource(is));
					} else {
						// Volume volume = this.load(is);
						Volume volume = SeedUtils.load(new File(source));
						document = SeedToXmlDocumentConverter.getInstance().convert(volume);
					}

					if (document == null) {
						continue;
					}
				}
				ruleEngineService.executeAllRules(document, context, new Action() {
					@Override
					public void update(RuleContext context, Message message) {
						message.setSource(uri);
						context.addViolation(message);
					}
				});
			}
			Map<Integer, List<Message>> map = context.map();
			if (map != null && !map.isEmpty()) {
				SortedSet<Integer> keys = new TreeSet<>(map.keySet());

				if (ps instanceof ReportPrintStream) {
					StringBuffer buffer = new StringBuffer();
					buffer.append("Summary:").append(System.lineSeparator());
					buffer.append("=================================================================================")
							.append(System.lineSeparator());

					for (Integer key : keys) {
						List<Message> list = map.get(key);
						String description = list.get(0).getRule().getDescription();
						buffer.append(String.format("%-5d|%-6d|%s", list.size(), key, description)).append("\n");
					}
					buffer.append("=================================================================================")
							.append(System.lineSeparator());
					ps.printHeader(buffer.toString());
				} else {
					ps.printHeader();
				}

				try {
					for (Integer key : keys) {
						List<Message> l = map.get(key);
						for (Message m : l) {
							ps.print(m);
							ps.flush();
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				ps.printMessage("PASSED");
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
			ps.printFooter();
		} catch (Exception e) {
			if (source != null) {
				System.out.println(source);
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Volume load(File file) throws Exception {
		try (final FileInputStream inputStream = new FileInputStream(file)) {
			return load(inputStream);
		}
	}

	public Volume load(InputStream inputStream) throws Exception {

		BlocketteDirector director = new BlocketteDirector(new BlocketteBuilder());
		BlocketteItrator iterator = director.process(inputStream);

		Volume volume = new Volume();
		while (iterator.hasNext()) {
			Blockette blockette = iterator.next();
			volume.add(blockette);
		}
		return volume;
	}

	private RuleResultPrintStream getOutputStream(String format, OutputStream outputStream) throws IOException {
		if (format == null || format.isEmpty() || "html".equalsIgnoreCase(format)) {
			return new HtmlPrintStream(outputStream);
		} else if ("csv".equalsIgnoreCase(format)) {
			return new CsvPrintStream(outputStream);
		} else if ("xml".equalsIgnoreCase(format)) {
			return new XmlPrintStream(outputStream);
		} else if ("report".equalsIgnoreCase(format)) {
			return new ReportPrintStream(outputStream);
		} else {
			throw new IOException("Invalid format [" + format + "] requested");
		}
	}

	private Unmarshaller theMarshaller() throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(edu.iris.dmc.fdsn.station.model.ObjectFactory.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			StreamSource stream = new StreamSource(
					Application.class.getClassLoader().getResourceAsStream("fdsn-station-1.0.xsd"));
			SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(stream);
			// u.setSchema(schema);
			// u.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper",
			// new DefaultNamespacePrefixMapper());
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

		RuleEngineService ruleEngineService = new RuleEngineService(null);
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

}
