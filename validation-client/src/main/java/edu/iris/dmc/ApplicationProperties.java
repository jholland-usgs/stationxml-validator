package edu.iris.dmc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ApplicationProperties {

	private int minLatitude;
	private int maxLatitude;
	private int minLongitude;
	private int maxLongitude;
	private int minElevation;
	private int maxElevation;
	private int minAzimuth;
	private int maxAzimuth;
	private int minDip;
	private int maxDip;
	private int maxDistance;
	private String[] units;

	private String result = "";
	private InputStream inputStream;

	public ApplicationProperties(String file) throws IOException {
		init(file);
	}

	private void init(String file) throws IOException {
		try {
			Properties prop = new Properties();
			String propFileName = "application.properties";

			if (file == null) {
				inputStream = new FileInputStream(new File(file));
			} else {
				propFileName = file;
				inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			}

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			String temp = prop.getProperty("minlatitude");
			if (temp != null) {
				setMinLatitude(Integer.parseInt(temp));
			}
			temp = prop.getProperty("max-latitude");
			if (temp != null) {
				setMaxLatitude(Integer.parseInt(temp));
			}
			temp = prop.getProperty("min-longitude");
			if (temp != null) {
				setMinLongitude(Integer.parseInt(temp));
			}
			temp = prop.getProperty("max-longitude");
			if (temp != null) {
				setMaxLongitude(Integer.parseInt(temp));
			}

			temp = prop.getProperty("min-elevation");
			if (temp != null) {
				setMinElevation(Integer.parseInt(temp));
			}
			temp = prop.getProperty("max-elevation");
			if (temp != null) {
				setMaxElevation(Integer.parseInt(temp));
			}
			temp = prop.getProperty("min-azimuth");
			if (temp != null) {
				setMinAzimuth(Integer.parseInt(temp));
			}
			temp = prop.getProperty("max-azimuth");
			if (temp != null) {
				setMaxAzimuth(Integer.parseInt(temp));
			}
			temp = prop.getProperty("min-dip");
			if (temp != null) {
				setMinDip(Integer.parseInt(temp));
			}
			temp = prop.getProperty("max-dip");
			if (temp != null) {
				setMaxDip(Integer.parseInt(temp));
			}
			temp = prop.getProperty("max-distance");
			if (temp != null) {
				setMaxDistance(Integer.parseInt(temp));
			}
			temp = prop.getProperty("units");
			if (temp != null) {
				if (!temp.isEmpty()) {
					setUnits(temp.split(","));
				}
			}

		} finally {
			inputStream.close();
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(int minLatitude) {
		this.minLatitude = minLatitude;
	}

	public int getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(int maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public int getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(int minLongitude) {
		this.minLongitude = minLongitude;
	}

	public int getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(int maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public int getMinElevation() {
		return minElevation;
	}

	public void setMinElevation(int minElevation) {
		this.minElevation = minElevation;
	}

	public int getMaxElevation() {
		return maxElevation;
	}

	public void setMaxElevation(int maxElevation) {
		this.maxElevation = maxElevation;
	}

	public int getMinAzimuth() {
		return minAzimuth;
	}

	public void setMinAzimuth(int minAzimuth) {
		this.minAzimuth = minAzimuth;
	}

	public int getMaxAzimuth() {
		return maxAzimuth;
	}

	public void setMaxAzimuth(int maxAzimuth) {
		this.maxAzimuth = maxAzimuth;
	}

	public int getMinDip() {
		return minDip;
	}

	public void setMinDip(int minDip) {
		this.minDip = minDip;
	}

	public int getMaxDip() {
		return maxDip;
	}

	public void setMaxDip(int maxDip) {
		this.maxDip = maxDip;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}

	public String[] getUnits() {
		return units;
	}

	public void setUnits(String[] units) {
		this.units = units;
	}

}
