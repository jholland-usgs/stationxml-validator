package edu.iris.dmc.seed.convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import edu.iris.dmc.TimeUtil;
import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.fdsn.station.model.Coefficients;
import edu.iris.dmc.fdsn.station.model.Comment;
import edu.iris.dmc.fdsn.station.model.Decimation;
import edu.iris.dmc.fdsn.station.model.Equipment;
import edu.iris.dmc.fdsn.station.model.FDSNStationXML;
import edu.iris.dmc.fdsn.station.model.FIR;
import edu.iris.dmc.fdsn.station.model.Gain;
import edu.iris.dmc.fdsn.station.model.Network;
import edu.iris.dmc.fdsn.station.model.PolesZeros;
import edu.iris.dmc.fdsn.station.model.Polynomial;
import edu.iris.dmc.fdsn.station.model.ResponseList;
import edu.iris.dmc.fdsn.station.model.ResponseStage;
import edu.iris.dmc.fdsn.station.model.Station;
import edu.iris.dmc.fdsn.station.model.Units;
import edu.iris.dmc.seed.Blockette;
import edu.iris.dmc.seed.SeedException;
import edu.iris.dmc.seed.Volume;
import edu.iris.dmc.seed.control.dictionary.B031;
import edu.iris.dmc.seed.control.dictionary.B033;
import edu.iris.dmc.seed.control.dictionary.B034;
import edu.iris.dmc.seed.control.station.B050;
import edu.iris.dmc.seed.control.station.B051;
import edu.iris.dmc.seed.control.station.B052;
import edu.iris.dmc.seed.control.station.B053;
import edu.iris.dmc.seed.control.station.B054;
import edu.iris.dmc.seed.control.station.B055;
import edu.iris.dmc.seed.control.station.B057;
import edu.iris.dmc.seed.control.station.B058;
import edu.iris.dmc.seed.control.station.B059;
import edu.iris.dmc.seed.control.station.B061;
import edu.iris.dmc.seed.control.station.B062;
import edu.iris.dmc.station.builder.ChannelBuilder;
import edu.iris.dmc.station.builder.CoefficientsBuilder;
import edu.iris.dmc.station.builder.CommentBuilder;
import edu.iris.dmc.station.builder.DecimationBuilder;
import edu.iris.dmc.station.builder.FirBuilder;
import edu.iris.dmc.station.builder.GainBuilder;
import edu.iris.dmc.station.builder.PolesZerosBuilder;
import edu.iris.dmc.station.builder.PolynomialBuilder;
import edu.iris.dmc.station.builder.ResponseListBuilder;
import edu.iris.dmc.station.builder.StationBuilder;

public class SeedToXmlDocumentConverter {

	private static SeedToXmlDocumentConverter INSTANCE = new SeedToXmlDocumentConverter();

	public static SeedToXmlDocumentConverter getInstance() {
		return INSTANCE;
	}

	public FDSNStationXML convert(Volume volume) throws SeedException, IOException {

		if (volume == null) {
			throw new IllegalArgumentException("Container cannot be null");
		}

		FDSNStationXML document = new FDSNStationXML();
		try {
			document.setSource("IRIS-DMC");
			document.setCreated(TimeUtil.now());
			document.setSchemaVersion(BigDecimal.valueOf(1.0));
			document.setModule("IRIS converter | version: ");
			document.setModuleURI("https://seiscode.iris.washington.edu/projects/stationxml-converter/wiki");

			volume.getB010();
			// volume.getB011();

			Network network = null;
			Station station = null;
			Channel channel = null;
			ResponseStage stage = null;
			for (Blockette blockette : volume.getControlBlockettes()) {
				final int type = blockette.getType();
				switch (type) {
				case 50:
					station = StationBuilder.build((B050) blockette);
					String networkCode = ((B050) blockette).getNetworkCode();
					int networkIdentifierCode = ((B050) blockette).getNetworkIdentifierCode();
					if (network == null || !networkCode.equals(network.getCode())) {
						network = new Network();
						network.setCode(networkCode);
						B033 b03310 = (B033) volume.getDictionaryBlockette(33, networkIdentifierCode);
						network.setDescription(b03310.getDescription());
						document.getNetwork().add(network);
					}
					network.addStation(station);
					break;
				case 51:
					B051 b051 = (B051) blockette;
					Comment stationComment = CommentBuilder.buildForStation(b051);
					B031 b031 = (B031) volume.getDictionaryBlockette(31, b051.getLookupKey());
					stationComment.setValue(b031.getDescription());
					station.add(stationComment);
					break;
				case 52:
					B052 b052 = (B052) blockette;
					channel = ChannelBuilder.build((B052) blockette);
					station.addChannel(channel);
					B033 b03306 = (B033) volume.getDictionaryBlockette(33, b052.getInstrumentIdentifier());

					if (b03306 != null) {
						Equipment equipment = new Equipment();
						equipment.setDescription(b03306.getDescription());
						channel.setSensor(equipment);
					}
					B034 b03408 = (B034) volume.getDictionaryBlockette(34, b052.getUnitsOfSignalResponse());
					if (b03408 != null) {
						Units units = new Units();
						units.setName(b03408.getName());
						units.setDescription(b03408.getDescription());
					}

					B034 b03409 = (B034) volume.getDictionaryBlockette(34, b052.getUnitsOfCalibrationInput());
					if (b03409 != null) {
						Units units = new Units();
						units.setName(b03409.getName());
						units.setDescription(b03409.getDescription());
						channel.setCalibrationUnits(units);
					}

					// Not needed for station xml
					b052.getDataFormatIdentifier();
					b052.getOptionalComment();
					break;
				case 53:
					B053 b053 = (B053) blockette;
					if (stage == null || stage.getNumber().intValue() != b053.getStageSequence()) {
						stage = new ResponseStage();
						stage.setNumber(BigInteger.valueOf(b053.getStageSequence()));
						channel.addStage(stage);
					}

					PolesZeros polesZeros = PolesZerosBuilder.build(b053);
					b053.getStageSequence();

					B034 b03405 = (B034) volume.getDictionaryBlockette(34, b053.getSignalInputUnit());
					if (b03405 != null) {
						Units ut = new Units();
						ut.setDescription(b03405.getDescription());
						ut.setName(b03405.getName());
						polesZeros.setInputUnits(ut);
					}
					B034 b03406 = (B034) volume.getDictionaryBlockette(34, b053.getSignalOutputUnit());
					if (b03406 != null) {
						Units ut = new Units();
						ut.setDescription(b03406.getDescription());
						ut.setName(b03406.getName());
						polesZeros.setOutputUnits(ut);
					}
					stage.add(polesZeros);
					break;
				case 54:
					B054 b054 = (B054) blockette;
					if (stage == null || stage.getNumber().intValue() != b054.getStageSequence()) {
						stage = new ResponseStage();
						stage.setNumber(BigInteger.valueOf(b054.getStageSequence()));
						channel.addStage(stage);
					}

					Coefficients coefficients = CoefficientsBuilder.build(b054);
					b03405 = (B034) volume.getDictionaryBlockette(34, b054.getSignalInputUnit());
					if (b03405 != null) {
						Units ut = new Units();
						ut.setDescription(b03405.getDescription());
						ut.setName(b03405.getName());
						coefficients.setInputUnits(ut);
					}
					b03406 = (B034) volume.getDictionaryBlockette(34, b054.getSignalOutputUnit());
					if (b03406 != null) {
						Units ut = new Units();
						ut.setDescription(b03406.getDescription());
						ut.setName(b03406.getName());
						coefficients.setOutputUnits(ut);
					}
					stage.add(coefficients);
					break;
				case 55:
					B055 b055 = (B055) blockette;
					if (stage == null || stage.getNumber().intValue() != b055.getStageSequence()) {
						stage = new ResponseStage();
						stage.setNumber(BigInteger.valueOf(b055.getStageSequence()));
						channel.addStage(stage);
					}
					ResponseList responseList = ResponseListBuilder.build(b055);

					b055.getSignalInputUnit();
					b055.getSignalOutputUnit();

					b03405 = (B034) volume.getDictionaryBlockette(34, b055.getSignalInputUnit());
					if (b03405 != null) {
						Units ut = new Units();
						ut.setDescription(b03405.getDescription());
						ut.setName(b03405.getName());
						responseList.setInputUnits(ut);
					}
					b03406 = (B034) volume.getDictionaryBlockette(34, b055.getSignalOutputUnit());
					if (b03406 != null) {
						Units ut = new Units();
						ut.setDescription(b03406.getDescription());
						ut.setName(b03406.getName());
						responseList.setOutputUnits(ut);
					}
					stage.add(responseList);
					break;
				case 57:
					B057 b057 = (B057) blockette;
					if (stage == null || stage.getNumber().intValue() != b057.getStageSequence()) {
						stage = new ResponseStage();
						stage.setNumber(BigInteger.valueOf(b057.getStageSequence()));
						channel.addStage(stage);
					}
					Decimation decimation = DecimationBuilder.build(b057);
					stage.setDecimation(decimation);
					break;
				case 58:
					B058 b058 = (B058) blockette;
					if (stage == null || stage.getNumber().intValue() != b058.getStageSequence()) {
						stage = new ResponseStage();
						stage.setNumber(BigInteger.valueOf(b058.getStageSequence()));
						channel.addStage(stage);
					}
					Gain gain = GainBuilder.build(b058);
					stage.setStageGain(gain);
					break;
				case 59:
					B059 b059 = (B059) blockette;
					Comment channelComment = CommentBuilder.buildForChannel(b059);
					b031 = (B031) volume.getDictionaryBlockette(31, b059.getLookupKey());
					channelComment.setValue(b031.getDescription());
					channel.add(channelComment);
					break;
				case 60:
					break;
				case 61:
					B061 b061 = (B061) blockette;
					if (stage == null || stage.getNumber().intValue() != b061.getStageSequence()) {
						stage = new ResponseStage();
						stage.setNumber(BigInteger.valueOf(b061.getStageSequence()));
						channel.addStage(stage);
					}
					FIR fir = FirBuilder.build(b061);
					b03405 = (B034) volume.getDictionaryBlockette(34, b061.getSignalInputUnit());
					if (b03405 != null) {
						Units ut = new Units();
						ut.setDescription(b03405.getDescription());
						ut.setName(b03405.getName());
						fir.setInputUnits(ut);
					}
					b03406 = (B034) volume.getDictionaryBlockette(34, b061.getSignalOutputUnit());
					if (b03406 != null) {
						Units ut = new Units();
						ut.setDescription(b03406.getDescription());
						ut.setName(b03406.getName());
						fir.setOutputUnits(ut);
					}
					stage.add(fir);
					break;
				case 62:
					B062 b062 = (B062) blockette;
					if (stage == null || stage.getNumber().intValue() != b062.getStageSequence()) {
						stage = new ResponseStage();
						stage.setNumber(BigInteger.valueOf(b062.getStageSequence()));
						channel.addStage(stage);
					}
					Polynomial polynomial = PolynomialBuilder.build(b062);

					b03405 = (B034) volume.getDictionaryBlockette(34, b062.getSignalInputUnit());
					if (b03405 != null) {
						Units ut = new Units();
						ut.setDescription(b03405.getDescription());
						ut.setName(b03405.getName());
						polynomial.setInputUnits(ut);
					}
					b03406 = (B034) volume.getDictionaryBlockette(34, b062.getSignalOutputUnit());
					if (b03406 != null) {
						Units ut = new Units();
						ut.setDescription(b03406.getDescription());
						ut.setName(b03406.getName());
						polynomial.setOutputUnits(ut);
					}
					stage.add(polynomial);
					break;
				default:
					break;
				}
			}
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}


}
