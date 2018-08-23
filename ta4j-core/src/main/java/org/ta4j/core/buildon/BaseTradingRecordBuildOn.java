package org.ta4j.core.buildon;

import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BaseTradingRecordBuildOn extends BaseTradingRecord implements TradingRecordBuildOn {

    private static final long serialVersionUID = -4436851731855891220L;

    /*
    * CurrentTrade is always the peek of the openedTrades.
    * So anytime you make changes to openedTrades you have to run refreshTrade().
    */
    protected PriorityQueue<Trade> openedTrades = new PriorityQueue<>(new Fifo());

    /**
     * Constructor.
     */
    public BaseTradingRecordBuildOn() {
        this(Order.OrderType.BUY);
    }

    /**
     * Constructor.
     *
     * @param entryOrderType the {@link Order.OrderType order type} of entries in the trading session
     */
    public BaseTradingRecordBuildOn(Order.OrderType entryOrderType) {
        if (entryOrderType == null) {
            throw new IllegalArgumentException("Starting type must not be null");
        }
        this.startingType = entryOrderType;
        currentTrade = new Trade(entryOrderType);
    }

    /**
     * Constructor.
     *
     * @param orders the orders to be recorded (cannot be empty)
     */
    private BaseTradingRecordBuildOn(Order... orders) {
        this(orders[0].getType());
        for (Order o : orders) {
            boolean newOrderWillBeAnEntry = currentTrade.isNew();
            if (newOrderWillBeAnEntry && o.getType() != startingType) {
                // Special case for entry/exit types reversal
                // E.g.: BUY, SELL,
                //    BUY, SELL,
                //    SELL, BUY,
                //    BUY, SELL
                currentTrade = new Trade(o.getType());
            }
            Order newOrder = currentTrade.operate(o.getIndex(), o.getPrice(), o.getAmount());
            recordOrder(newOrder, newOrderWillBeAnEntry);
        }
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
    public void operate(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isClosed()) {
            // Current trade closed, should not occur
            throw new IllegalStateException("Current trade should not be closed");
        }
        Trade operatedTrade = getCurrentTrade();
        boolean newOrderWillBeAnEntry = operatedTrade.isNew();
        Order newOrder = operatedTrade.operate(index, price, amount);
        if (newOrderWillBeAnEntry) {
            // No refresh is needed, because it is the first element of the empty list
            openedTrades.add(operatedTrade);
        }
        recordOrder(newOrder, newOrderWillBeAnEntry);
    }

    public void operateBuildOn(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isClosed()) {
            // Current trade closed, should not occur
            throw new IllegalStateException("Current trade should not be closed " +
                    "or openedTrades list is empty");
        }
        Trade builtOnTrade = new Trade(this.startingType);
        Order newOrder = builtOnTrade.operate(index, price, amount);
        openedTrades.add(builtOnTrade);
        refreshTrade();
        recordOrder(newOrder, true);
    }

    @Override
    public boolean enter(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isNew()) {
            operate(index, price, amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean buildOn(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isOpened()) {
            operateBuildOn(index, price, amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean exit(int index, Decimal price, Decimal amount) {
        if (getCurrentTrade().isOpened()) {
            operate(index, price, amount);
            return true;
        }
        return false;
    }

    /**
     * Records an order and the corresponding trade (if closed).
     *
     * @param order   the order to be recorded
     * @param isEntry true if the order is an entry, false otherwise (exit)
     */
    protected void recordOrder(Order order, boolean isEntry) {
        if (order == null) {
            throw new IllegalArgumentException("Order should not be null");
        }

        // Storing the new order in entries/exits lists
        if (isEntry) {
            entryOrders.add(order);
        } else {
            exitOrders.add(order);
        }

        // Storing the new order in orders list
        orders.add(order);
        if (Order.OrderType.BUY.equals(order.getType())) {
            // Storing the new order in buy orders list
            buyOrders.add(order);
        } else if (Order.OrderType.SELL.equals(order.getType())) {
            // Storing the new order in sell orders list
            sellOrders.add(order);
        }

        // Storing the trade if closed
        if (getCurrentTrade().isClosed()) {
            trades.add(getCurrentTrade());
            openedTrades.remove(getCurrentTrade());
            refreshTrade();
        }
    }

    private class Fifo implements Comparator<Trade> {

        @Override
        public int compare(Trade o1, Trade o2) {
            return Integer.compare(o2.getEntry().getIndex(), o1.getEntry().getIndex());
        }
    }
}
