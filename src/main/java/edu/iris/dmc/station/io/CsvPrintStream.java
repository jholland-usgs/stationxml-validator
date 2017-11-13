package edu.iris.dmc.station.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.iris.dmc.station.XmlUtil;
import edu.iris.dmc.station.rules.Result;

public class CsvPrintStream extends PrintStream implements RuleResultPrintStream{

	private static final Object[] FILE_HEADER = { "Source","RuleId", "Network", "Station", "Channel", "Location", "StartDate",
			"EndDate", "Message" };
	private CSVPrinter csvFilePrinter = null;

	public CsvPrintStream(OutputStream out) throws IOException {
		super(out);
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator());
		this.csvFilePrinter = new CSVPrinter(this, csvFileFormat);
	}

	public void printHeader() throws IOException {
		csvFilePrinter.printRecord(FILE_HEADER);
	}
	public void print(Result result) throws IOException {
		print("", result);
	}
	public void print(String source,Result result) throws IOException {
		List<String> record = new ArrayList<>();

		record.add(source);
		record.add("" + result.getRuleId());
		record.add(result.getNetwork() == null ? null : result.getNetwork().getCode());
		record.add(result.getStation() == null ? null : result.getStation().getCode());
		record.add(result.getChannel() == null ? null : result.getChannel().getCode());
		record.add(result.getChannel() == null ? null : result.getChannel().getLocationCode());

		if (result.getChannel() != null) {
			record.add(result.getChannel() == null ? null : XmlUtil.toText(result.getChannel().getStartDate()));
			record.add(result.getChannel() == null ? null : XmlUtil.toText(result.getChannel().getEndDate()));
		} else if (result.getStation() != null) {
			record.add(XmlUtil.toText(result.getStation().getStartDate()));
			record.add(XmlUtil.toText(result.getStation().getEndDate()));
		} else if (result.getNetwork() != null) {
			record.add(XmlUtil.toText(result.getNetwork().getStartDate()));
			record.add(XmlUtil.toText(result.getNetwork().getEndDate()));
		}

		record.add(result.getMessage());
		csvFilePrinter.printRecord(record);
		csvFilePrinter.flush();

	}

	@Override
	public void printFooter() {

	}

	@Override
	public void printRow(String text) {
		this.println(text);
	}

	
	@Override
	public void printMessage(String text) {
		this.print(text);
	}

}
