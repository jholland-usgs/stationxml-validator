package edu.iris.dmc;

import com.beust.jcommander.Parameter;

public class Args {

	@Parameter(names = { "--help", "-h" }, description = "Print help")
	boolean help = false;

	@Parameter(names = { "--version", "-v" }, description = "Print version number")
	boolean version = false;

	@Parameter(names = "--debug", description = "Debug mode")
	boolean debug = false;


	@Parameter(names = "--ignore-warnings", description = "Ignore warnings")
	boolean ignoreWarnings = false;
	@Parameter(names = { "--rules", "-r" }, description = "Print rules")
	boolean printRules = false;
	@Parameter(names = { "--units", "-u" }, description = "Print units")
	boolean printUnits = false;

	@Parameter(names = { "--level", "-l" }, description = "[net|sta|cha|resp]default is resp")
	String level = "resp";
	
	@Parameter(names = { "--format", "-f" }, description = "[csv|html|xml]default is html")
	String format = "html";

	@Parameter(names = { "--summary", "-s" }, description = "Print summary result")
	boolean summary = false;

	@Parameter(names = { "--output", "-o" }, description = "Where to print")
	String output;
	
	@Parameter(description = "Input")
	String input;

}
