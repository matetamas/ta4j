package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.TradingRecord;

public interface TradingRecordBuildOn extends TradingRecord {

    boolean buildOn(int index, Decimal price, Decimal amount);
}
