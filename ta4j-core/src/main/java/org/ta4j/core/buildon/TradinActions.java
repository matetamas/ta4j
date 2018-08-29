package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;

import java.util.List;

public class TradinActions implements TradingRecord {
    private TradingManager tradingManager;
    private TradingRecordBuildOn tradingRecordBuildOn;

    @Override
    public Trade getCurrentTrade() {
        return null;
    }

    @Override
    public void operate(int index, Decimal price, Decimal amount) {

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
        return null;
    }

    @Override
    public Order getLastOrder() {
        return null;
    }

    @Override
    public Order getLastOrder(Order.OrderType orderType) {
        return null;
    }

    @Override
    public Order getLastEntry() {
        return null;
    }

    @Override
    public Order getLastExit() {
        return null;
    }
}
