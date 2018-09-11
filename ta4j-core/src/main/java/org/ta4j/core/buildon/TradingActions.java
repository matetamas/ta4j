package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;

import java.util.List;

public class TradingActions implements TradingRecord {
    private TradingManager tradingManager;
    private TradingRecordBuildOn tradingRecordBuildOn;
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
    @Deprecated
    public void operate(int index, Decimal price, Decimal amount) {
        throw new IllegalStateException("Operate method can't be used");
    }

    @Override
    public boolean enter(int index, Decimal price, Decimal amount) {
        return false;
    }

    @Override
    public boolean exit(int index, Decimal price, Decimal amount) {
        return false;
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
