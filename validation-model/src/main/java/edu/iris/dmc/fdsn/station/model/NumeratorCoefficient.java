package edu.iris.dmc.fdsn.station.model;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
public class NumeratorCoefficient {

	@XmlValue
	protected double value;
	@XmlAttribute(name = "i")
	protected BigInteger i;

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
	 * Gets the value of the i property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getI() {
		return i;
	}

	/**
	 * Sets the value of the i property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setI(BigInteger value) {
		this.i = value;
	}

}
