package edu.iris.dmc.station.conditions;

import java.util.logging.Logger;

import edu.iris.dmc.fdsn.station.model.Channel;
import edu.iris.dmc.station.restrictions.Restriction;

public abstract class ChannelRestrictedCondition extends AbstractCondition {

	private static final Logger LOGGER = Logger.getLogger(ChannelRestrictedCondition.class.getName());

	protected Restriction[] restrictions;

	public ChannelRestrictedCondition(boolean required, String description) {
		this(required, description, null);
	}

	public ChannelRestrictedCondition(boolean required, String description, Restriction[] restrictions) {
		super(required, description);
		this.restrictions = restrictions;
	}

	public boolean isRestricted(Channel channel) {
		if (restrictions != null) {
			for (Restriction restriction : restrictions) {
				if (restriction.qualifies(channel)) {
					return true;
				}
			}
		}
		return false;
	}
}
