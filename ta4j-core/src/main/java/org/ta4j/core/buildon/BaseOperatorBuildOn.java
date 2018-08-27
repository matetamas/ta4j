package org.ta4j.core.buildon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;

import static org.ta4j.core.buildon.BaseStrategyBuildOn.*;

public class BaseOperatorBuildOn implements OperatorBuildOn {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private StrategyBuildOn strategy;
    private StrategyAction action;
    private boolean isAggressive;

    public BaseOperatorBuildOn(StrategyBuildOn strategy) {
        this(strategy,true);
    }

    public BaseOperatorBuildOn(StrategyBuildOn strategy, boolean isAggressive) {
        if (strategy == null)
            throw new IllegalArgumentException("Strategy should not be null!");
        this.strategy = strategy;
        this.action = StrategyAction.NOTHING;
        this.isAggressive = isAggressive;
    }

    @Override
    public boolean operate(int index, TradingRecord tradingRecord) {
        Trade trade = tradingRecord.getCurrentTrade();
        if (trade.isNew()) {
            return shouldEnter(index, tradingRecord);
        } else if (trade.isOpened() && isAggressive) {
            return shouldBuildOnOrExitAggressive(index, tradingRecord);
        } else if (trade.isOpened() && !isAggressive) {
            return shouldBuildOnOrExitDefensive(index, tradingRecord);
        } else {
            return false;
        }
    }

    private boolean shouldEnter(int index, TradingRecord tradingRecord) {
        // Our strategy should enter
        if (strategy.shouldEnter(index, tradingRecord)) {
            LOG.trace("Strategy should ENTER on " + index);
            setAction(StrategyAction.ENTER);
            return true;
        } else {
            setAction(StrategyAction.NOTHING);
            return false;
        }
    }

    private boolean shouldBuildOnOrExitAggressive(int index, TradingRecord tradingRecord) {
        // Our strategy should build on or exit opened position
        if (strategy.shouldBuildOn(index, tradingRecord)) {
            LOG.trace("Strategy should BUILD ON on " + index);
            setAction(StrategyAction.BUILDON);
            return true;
        } else if (strategy.shouldExit(index, tradingRecord)) {
            LOG.trace("Strategy should EXIT on " + index);
            setAction(StrategyAction.EXIT);
            return true;
        } else {
            setAction(StrategyAction.NOTHING);
            return false;
        }
    }

    private boolean shouldBuildOnOrExitDefensive(int index, TradingRecord tradingRecord) {
        // Our strategy should exit or build on opened position
        if (strategy.shouldExit(index, tradingRecord)) {
            LOG.trace("Strategy should EXIT on " + index);
            setAction(StrategyAction.EXIT);
            return true;
        } else if (strategy.shouldBuildOn(index, tradingRecord)) {
            LOG.trace("Strategy should BUILD ON on " + index);
            setAction(StrategyAction.BUILDON);
            return true;
        } else {
            setAction(StrategyAction.NOTHING);
            return false;
        }
    }

    public void setAggressive() {
        this.isAggressive = true;
    }

    public void setDefensive() {
        this.isAggressive = false;
    }

    public StrategyAction getAction() {return action;}

    protected void setAction(StrategyAction action) {this.action = action;}
}
