package org.ta4j.core.buildon;

public abstract class Constants {
	public static final Double FUND = 1000000.0;
	public static final Double LOT = 100000.0;
	public static final Double NORMAL_COMMISSION_RATE = 0.003;
	public static final Double DAY_TRADE_COMMISSION_RATE = 0.002;
	public static final Double NORMAL_MIN_COMMISSION = 199.0;
	public static final Double DAY_TRADE_MIN_COMMISSION = 199.0;
	public static final Double HIGHPRECISION = 1.0E-10;
	public static final Double DEFAULT_STOP_LEVEL = 0.5;
	public static final Double DEFAULT_WARN_LEVEL = 0.75; // TODO: 2018. 02. 17. should be used in the strategy to send message

	private Constants() {
	}
}
