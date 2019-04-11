package edu.iris.dmc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;

import edu.iris.dmc.CommandLine;
import edu.iris.dmc.CommandLineParseException;

public class CommandLine {

	private Path file;
	private Path output;
	private boolean ignoreWarnings;
	private int[] ignoreRules;
	private boolean showRules;
	private boolean showUnits;
	private String format;
	private boolean summary;
	private boolean showHelp;
	private boolean showVersion;

	private Level logLevel = Level.OFF;

	private Map<String, String> map = new HashMap<>();

	public Path file() {
		return file;
	}

	public Path output() {
		return output;
	}

	public boolean showHelp() {
		return showHelp;
	}

	public boolean showVersion() {
		return showVersion;
	}

	public boolean ignoreWarnings() {
		return ignoreWarnings;
	}

	public int[] ignoreRules() {
		return ignoreRules;
	}

	public boolean showRules() {
		return showRules;
	}

	public boolean showUnits() {
		return showUnits;
	}

	public Level getLogLevel() {
		return logLevel;
	}

	public static CommandLine parse(String[] args) throws CommandLineParseException {
		if (args == null || args.length == 0) {
			throw new CommandLineParseException("Application arguments cannot be empty or null!");
		}
		CommandLine commandLine = new CommandLine();
		// look for showHelp or showVersion flags
		if (args.length == 1) {
			if ("--help".equalsIgnoreCase(args[0]) || "--showhelp".equalsIgnoreCase(args[0])
					|| "-h".equalsIgnoreCase(args[0])) {
				commandLine.showHelp = true;
			} else if ("--version".equalsIgnoreCase(args[0]) || "-v".equalsIgnoreCase(args[0])) {
				commandLine.showVersion = true;
			}
			return commandLine;
		}
		if (args.length < 1) {
			throw new CommandLineParseException(
					"Invalid number of arguments, expected at least 1 but was " + args.length + "!");
		}
		String path = args[0];
		commandLine.file = Paths.get(path);

		if (!commandLine.file.toFile().exists()) {
			throw new CommandLineParseException(String.format("File %s does not exist!", path));
		}

		// look for logLevel
		if (args.length > 2) {
			for (int i = 1; i < args.length; i++) {
				String arg = args[i];
				if ("--help".equalsIgnoreCase(arg) || "--showhelp".equalsIgnoreCase(arg)
						|| "-h".equalsIgnoreCase(arg)) {
					commandLine.showHelp = true;
				} else if ("--version".equalsIgnoreCase(arg) || "-v".equalsIgnoreCase(arg)) {
					commandLine.showVersion = true;
				} else if ("--info".equalsIgnoreCase(arg)) {
					commandLine.logLevel = Level.INFO;
				} else if ("--debug".equalsIgnoreCase(arg)) {
					commandLine.logLevel = Level.FINE;
				} else if ("--ignore-warnings".equalsIgnoreCase(arg)) {
					commandLine.ignoreWarnings = true;
				} else if ("--ignore-rules".equalsIgnoreCase(arg)) {
					String rules = args[i + 1];
					commandLine.ignoreRules = Stream.of(rules.split("\\s*,\\s*")).map(String::trim)
							.map(Integer::parseInt).mapToInt(item -> item).toArray();
					i = i + 1;
				} else if ("--show-rules".equalsIgnoreCase(arg)) {
					commandLine.showRules = true;
				} else if ("--show-units".equalsIgnoreCase(arg)) {
					commandLine.showUnits = true;
				} else if ("--output".equalsIgnoreCase(arg) || "-o".equalsIgnoreCase(arg)) {
					commandLine.output = Paths.get(args[i + 1]);
					i = i + 1;
				}

			}
		}

		return commandLine;
	}

}
