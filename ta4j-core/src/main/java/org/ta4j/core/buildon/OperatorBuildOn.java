package org.ta4j.core.buildon;

import org.ta4j.core.TradingRecord;

public interface OperatorBuildOn {
    boolean operate(int index, TradingRecord tradingRecord);
}