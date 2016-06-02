package edu.iris.dmc.validation.rule;

import javax.validation.Payload;

public class ErrorCodes {

	public static class Network {
		public interface Code extends Payload {
			public static int id = 101;
		}

		public interface Regex extends Payload {
			public static int id = 102;
		}

		public interface StartTime extends Payload {
			public static int id = 103;
		}

		//@Field("end_date")
		//@ErrorDescription("The end date must be greater than the start date")
		//public static final class EndDateMustBeGreaterThanStartDatePayload implements ApiResponseOverridePayload {
		//};

		public interface EpochRange extends Payload {
			public static int id = 105;
		}

		public interface EpochOverlap extends Payload {
			public static int id = 152;
		}
	}

	public static class Station {
		public interface Code extends Payload {
			public static int id = 201;
		}

		public interface Regex extends Payload {
			public static int id = 202;
		}

		public interface StartTime extends Payload {
			public static int id = 203;
		}

		public interface TimeRangeCheck extends Payload {
			public static int id = 205;
		}

		public interface LatitudeCheck extends Payload {
			public static int id = 206;
		}

		public interface LongitudeCheck extends Payload {
			public static int id = 207;
		}

		public interface ElevationCheck extends Payload {
			public static int id = 208;
		}

		public interface CreationTimeCheck extends Payload {
			public static int id = 209;
		}

		public interface DistanceCheck extends Payload {
			public static int id = 251;
		}

		public interface ChannelEpochOverlapCheck extends Payload {
			public static int id = 252;
		}
	}
}
