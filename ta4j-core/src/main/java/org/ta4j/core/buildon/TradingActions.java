package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.buildon.BaseStrategyBuildOn.StrategyAction;

import java.util.List;

public class TradingActions implements TradingRecord {
    private TradingManager tradingManager;
    private TradingRecordBuildOn tradingRecordBuildOn;
    protected StrategyAction action;
    private boolean isFinished;

    public TradingActions(TradingManager tradingManager, TradingRecordBuildOn tradingRecordBuildOn) {
        this.tradingManager = tradingManager;
        this.tradingRecordBuildOn = tradingRecordBuildOn;
        this.isFinished = false;
    }

    @Override
    public Trade getCurrentTrade() {
        return tradingManager.getCurrentTrade();
    }

    @Override
    public void operate(int index, Decimal price, Decimal amount) {
        operate(getAction(), index, price, amount);
    }

    public void operate(StrategyAction action, int index, Decimal price, Decimal amount) {
        if (StrategyAction.ENTER.equals(action)) {
            enter(index, price, amount);
        } else if (StrategyAction.BUILDON.equals(action)) {
            buildOn(index, price, amount);
        } else if (StrategyAction.EXIT.equals(action)) {
            exit(index, price, amount);
        }
    }

    @Override
    public boolean enter(int index, Decimal price, Decimal amount) {
        boolean isEntered = tradingManager.enter(index, price, amount);
        if (isEntered) {
            Trade openedTrade = tradingManager.getOpenTradeToRecord();
            tradingRecordBuildOn.recordTrade(openedTrade);
            return true;
        } else {
            return false;
        }
    }

    public boolean buildOn(int index, Decimal price, Decimal amount) {
        boolean isBuiltOn = tradingManager.buildOn(index, price, amount);
        if (isBuiltOn) {
            Trade openedTrade = tradingManager.getOpenTradeToRecord();
            tradingRecordBuildOn.recordTrade(openedTrade);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean exit(int index, Decimal price, Decimal amount) {
        boolean isExited = tradingManager.exit(index, price, amount);
        if (isExited) {
            List<Trade> closedTrades = tradingManager.getClosedTradesToRecord();
            tradingRecordBuildOn.recordListOfClosedTrades(closedTrades);
            return true;
        } else {
            return false;
        }
    }

    public void setAction(StrategyAction action) {
        this.action = action;
    }

    public StrategyAction getAction() {
        return this.action;
    }

    @Override
    public List<Trade> getTrades() {
        return tradingRecordBuildOn.getTrades();
    }

    @Override
    public Order getLastOrder() {
        return tradingRecordBuildOn.getLastOrder();
    }

    @Override
    public Order getLastOrder(Order.OrderType orderType) {
        return tradingRecordBuildOn.getLastOrder(orderType);
    }

    @Override
    public Order getLastEntry() {
        return tradingRecordBuildOn.getLastEntry();
    }

    @Override
    public Order getLastExit() {
        return tradingRecordBuildOn.getLastExit();
    }
}
