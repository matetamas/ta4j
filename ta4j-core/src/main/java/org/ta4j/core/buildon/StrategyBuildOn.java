package org.ta4j.core.buildon;

import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;

/**
* Missing overriding of and(), or() and opposite() methods
* */
public interface StrategyBuildOn extends Strategy {

    /**
     * @return the buildOn rule
     */
    Rule getBuildOnRule();

    /**
     * @param index the bar index
     * @return true to recommend to buildOn position, false otherwise
     */
    default boolean shouldBuildOn(int index) {
        return shouldBuildOn(index, null);
    }

    /**
     * @param index the bar index
     * @param tradingRecord the potentially needed trading history
     * @return true to recommend to buildOn position, false otherwise
     */
    default boolean shouldBuildOn(int index, TradingRecord tradingRecord) {
        if (isUnstableAt(index)) {
            return false;
        }
        return getBuildOnRule().isSatisfied(index, tradingRecord);
    }

    default boolean shouldOperate(int index, TradingRecord tradingRecord) {
        Trade trade = tradingRecord.getCurrentTrade();
        if (trade.isNew()) {
            return shouldEnter(index, tradingRecord);
        } else if (trade.isOpened()) {
            return shouldExitOrBuildOn(index, tradingRecord);
        }
        return false;
    }

    default boolean shouldExitOrBuildOn(int index, TradingRecord tradingRecord) {
        return shouldBuildOn(index, tradingRecord) || shouldExit(index, tradingRecord);
    }
}
