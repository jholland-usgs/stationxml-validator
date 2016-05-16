package edu.iris.dmc.fdsn.station.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Agency" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="Contact" type="{http://www.fdsn.org/xml/station/1}PersonType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="WebSite" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "agency", "contact", "webSite" })
public class Operator {

	@XmlElement(name = "Agency", required = true)
	protected List<String> agency;
	@XmlElement(name = "Contact")
	protected List<PersonType> contact;
	@XmlElement(name = "WebSite")
	@XmlSchemaType(name = "anyURI")
	protected String webSite;

	@XmlTransient
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the value of the agency property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the agency property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAgency().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getAgency() {
		if (agency == null) {
			agency = new ArrayList<String>();
		}
		return this.agency;
	}

	/**
	 * Gets the value of the contact property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the contact property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getContact().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PersonType }
	 * 
	 * 
	 */
	public List<PersonType> getContact() {
		if (contact == null) {
			contact = new ArrayList<PersonType>();
		}
		return this.contact;
	}

	/**
	 * Gets the value of the webSite property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getWebSite() {
		return webSite;
	}

	/**
	 * Sets the value of the webSite property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setWebSite(String value) {
		this.webSite = value;
	}

}
