package edu.iris.dmc.fdsn.station.model;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
public class Coefficient {

	@XmlAttribute(name = "number")
	protected BigInteger number;

	@XmlValue
	protected double value;
	@XmlAttribute(name = "plusError")
	protected Double plusError;
	@XmlAttribute(name = "minusError")
	protected Double minusError;

	/**
	 * Gets the value of the value property.
	 * 
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Gets the value of the plusError property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getPlusError() {
		return plusError;
	}

	/**
	 * Sets the value of the plusError property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setPlusError(Double value) {
		this.plusError = value;
	}

	/**
	 * Gets the value of the minusError property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getMinusError() {
		return minusError;
	}

	/**
	 * Sets the value of the minusError property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setMinusError(Double value) {
		this.minusError = value;
	}

	/**
	 * Gets the value of the number property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getNumber() {
		return number;
	}

	/**
	 * Sets the value of the number property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNumber(BigInteger value) {
		this.number = value;
	}

}
