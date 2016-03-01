package edu.iris.dmc.fdsn.station.model;

import java.sql.Timestamp;
import java.util.Date;

public interface NodeType {
	public String getCode();

	public Date getStartDate();

	public Date getEndDate();
}
