package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BaseTradingManager implements TradingManager { //TODO finish implementation

    /** The entry type (BUY or SELL) in the trading session */
    protected Order.OrderType startingType;

    /** The current non-closed trade (there's always one) */
    protected Trade currentTrade;

    /*
     * CurrentTrade is always the peek of the openedTrades.
     * So anytime you make changes to openedTrades you have to run refreshTrade().
     */
    protected PriorityQueue<Trade> openedTrades = new PriorityQueue<>(new Fifo());

    /**
     * Constructor.
     */
    public BaseTradingManager() {this(Order.OrderType.BUY);}

    /**
     * Constructor.
     *
     * @param entryOrderType the {@link Order.OrderType order type} of entries in the trading session
     */
    public BaseTradingManager(Order.OrderType entryOrderType) {
        if (entryOrderType == null) {
            throw new IllegalArgumentException("Starting type must not be null");
        }
        this.startingType = entryOrderType;
        currentTrade = new Trade(entryOrderType);
    }

    @Override
    public Trade getCurrentTrade() {
        return currentTrade;
    }

    protected void refreshTrade() {
        if (!openedTrades.isEmpty()) {
            currentTrade = openedTrades.peek();
        } else {
            currentTrade = new Trade(startingType);
        }
    }

    @Override
    public Trade operate(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isClosed()) {
            // Current trade closed, should not occur
            throw new IllegalStateException("Current trade should not be closed");
        }
        Trade operatedTrade = getCurrentTrade();
//        boolean newOrderWillBeAnEntry = operatedTrade.isNew();
        Order newOrder = operatedTrade.operate(index, price, amount);
        if (operatedTrade.isOpened()) {
            // Refresh is needed every time a new trade is added, has to recheck the currentTrade
            openedTrades.add(operatedTrade);
        } else if (operatedTrade.isClosed()) {
            openedTrades.remove(operatedTrade);
        }
        refreshTrade();
        return operatedTrade;
    }

    public Trade operateBuildOn(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isClosed()) {
            // Current trade closed, should not occur
            throw new IllegalStateException("Current trade should not be closed " +
                    "or openedTrades list is empty");
        }
        Trade builtOnTrade = new Trade(this.startingType);
        Order newOrder = builtOnTrade.operate(index, price, amount);
        openedTrades.add(builtOnTrade);
        refreshTrade();
        return builtOnTrade;
    }

    @Override
    public Trade enter(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isNew()) {
            return operate(index, price, amount);
        }
        return null;
    }

    @Override
    public Trade buildOn(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isOpened()) {
            return operateBuildOn(index, price, amount);
        }
        return null;
    }

    @Override
    public Trade exit(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isOpened()) {
            return operate(index, price, amount);
        }
        return null;
    }

    private class Fifo implements Comparator<Trade> {
        @Override
        public int compare(Trade o1, Trade o2) {
            return Integer.compare(o1.getEntry().getIndex(), o2.getEntry().getIndex());
        }
    }

    @Override
    public PriorityQueue<Trade> getOpenedTrades() {
        return openedTrades;
    }
}
