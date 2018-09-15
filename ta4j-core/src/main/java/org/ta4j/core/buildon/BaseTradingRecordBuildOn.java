package org.ta4j.core.buildon;

import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import java.util.ArrayList;
import java.util.List;

public class BaseTradingRecordBuildOn implements TradingRecordBuildOn {

    /** The recorded orders */
    protected List<Order> orders = new ArrayList<Order>();

    /** The recorded BUY orders */
    protected List<Order> buyOrders = new ArrayList<Order>();

    /** The recorded SELL orders */
    protected List<Order> sellOrders = new ArrayList<Order>();

    /** The recorded entry orders */
    protected List<Order> entryOrders = new ArrayList<Order>();

    /** The recorded exit orders */
    protected List<Order> exitOrders = new ArrayList<Order>();

    /** The recorded trades */
    protected List<Trade> trades = new ArrayList<Trade>();

    /** The entry type (BUY or SELL) in the trading session */
    protected Order.OrderType startingType;

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
    }

    @Override
    public void recordListOfClosedTrades(List<Trade> closedTrades) {
        for (Trade trade : closedTrades) {
            recordTrade(trade);
        }
    }

    /**
     * Records an order and the corresponding trade (if closed).
     *
     * @param trade   the trade, and order inside to be recorded
     */
    public void recordTrade(Trade trade) {
        Order order = null;
        if (trade == null) {
            throw new IllegalArgumentException("Trade should not be null");
        } else if (trade.isNew()) {
            throw new IllegalStateException("Trade should not be new");
        } else if (trade.isOpened()) {
            order = trade.getEntry();
        } else if (trade.isClosed()) {
            order = trade.getExit();
        }
        recordOrder(order);

        // Storing the trade if closed
        if (trade.isClosed()) {
            trades.add(trade);
        }
    }

    private void recordOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order should not be null");
        }
        // Storing the new order in entries/exits lists
        if (this.startingType.equals(order.getType())) {
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
    }

    @Override
    public List<Trade> getTrades() {
        return trades;
    }

    @Override
    public Order getLastOrder() {
        if (!orders.isEmpty()) {
            return orders.get(orders.size() - 1);
        }
        return null;
    }

    @Override
    public Order getLastOrder(Order.OrderType orderType) {
        if (Order.OrderType.BUY.equals(orderType) && !buyOrders.isEmpty()) {
            return buyOrders.get(buyOrders.size() - 1);
        } else if (Order.OrderType.SELL.equals(orderType) && !sellOrders.isEmpty()) {
            return sellOrders.get(sellOrders.size() - 1);
        }
        return null;
    }

    @Override
    public Order getLastEntry() {
        if (!entryOrders.isEmpty()) {
            return entryOrders.get(entryOrders.size() - 1);
        }
        return null;
    }

    @Override
    public Order getLastExit() {
        if (!exitOrders.isEmpty()) {
            return exitOrders.get(exitOrders.size() - 1);
        }
        return null;
    }
}
