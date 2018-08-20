package org.ta4j.core.buildon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Decimal;
import org.ta4j.core.Trade;

public class BaseOperatorBuildOn implements OperatorBuildOn {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private StrategyBuildOn strategy;
    private TradingRecordBuildOn tradingRecord;
    private boolean isAggressive;

    public BaseOperatorBuildOn(StrategyBuildOn strategy, TradingRecordBuildOn tradingRecord) {
        this(strategy, tradingRecord, true);
    }

    public BaseOperatorBuildOn(StrategyBuildOn strategy, TradingRecordBuildOn tradingRecord, boolean isAggressive) {
        if (strategy == null || tradingRecord == null)
            throw new IllegalArgumentException("Inputs should not be null!");
        this.strategy = strategy;
        this.tradingRecord = tradingRecord;
        this.isAggressive = isAggressive;
    }

    @Override
    public void operate(int index, Decimal price, Decimal amount) {
        Trade trade = tradingRecord.getCurrentTrade();
        if (trade.isNew()) {
            enter(index, price, amount);
        } else if (trade.isOpened() && isAggressive) {
            buildOnOrExitAggressive(index, price, amount);
        } else if (trade.isOpened() && !isAggressive) {
            buildOnOrExitDefensive(index, price, amount);
        }
    }

    private void enter(int index, Decimal closePrice, Decimal amount) {
        // Our strategy should enter
        if (strategy.shouldEnter(index, tradingRecord)) {
            LOG.trace("Strategy should ENTER on " + index);
            tradingRecord.enter(index, closePrice, amount);
        }
    }

    private void buildOnOrExitAggressive(int index, Decimal closePrice, Decimal amount) {
        // Our strategy should build on or exit opened position
        if (strategy.shouldBuildOn(index, tradingRecord)) {
            LOG.trace("Strategy should BUILD ON on " + index);
            tradingRecord.buildOn(index, closePrice, amount);
        } else if (strategy.shouldExit(index, tradingRecord)) {
            LOG.trace("Strategy should EXIT on " + index);
            tradingRecord.exit(index, closePrice, amount);
        }
    }

    private void buildOnOrExitDefensive(int index, Decimal closePrice, Decimal amount) {
        // Our strategy should exit or build on opened position
        if (strategy.shouldExit(index, tradingRecord)) {
            LOG.trace("Strategy should EXIT on " + index);
            tradingRecord.exit(index, closePrice, amount);
        } else if (strategy.shouldBuildOn(index, tradingRecord)) {
            LOG.trace("Strategy should BUILD ON on " + index);
            tradingRecord.buildOn(index, closePrice, amount);
        }
    }

    public void setAggressive() {
        this.isAggressive = true;
    }

    public void setDefensive() {
        this.isAggressive = false;
    }
}
