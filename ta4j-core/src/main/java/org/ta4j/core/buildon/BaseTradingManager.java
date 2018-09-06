package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static org.ta4j.core.buildon.BaseStrategyBuildOn.*;

public class BaseTradingManager implements TradingManager {

    /** The entry type (BUY or SELL) in the trading session */
    protected Order.OrderType startingType;

    /** The current non-closed trade (there's always one) */
    protected Trade currentTrade;

    protected Trade operatedTrade;

    /*
     * CurrentTrade is always the peek of the openedTrades.
     * So anytime you make changes to openedTrades you have to run refreshTrade().
     */
    protected PriorityQueue<Trade> openedTrades;

    protected List<Trade> closedTrades;

    protected StrategyAction action;

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
        this.currentTrade = new Trade(entryOrderType);
        this.openedTrades = new PriorityQueue<>(new Fifo());
        this.action = StrategyAction.NOTHING;
    }

    @Override
    public Trade getCurrentTrade() {
        return currentTrade;
    }

    @Override
    public PriorityQueue<Trade> getOpenedTrades() {
        return openedTrades;
    }

    protected void refreshTrade() {
        if (!openedTrades.isEmpty()) {
            currentTrade = openedTrades.peek();
        } else {
            currentTrade = new Trade(startingType);
        }
    }

    @Override
    public boolean enter(int index, Decimal price, Decimal amount) {
        operatedTrade = getCurrentTrade();
        Order newOrder;
        checkOperatedTradeState(operatedTrade);
        if (operatedTrade.isNew()) {
            newOrder = operatedTrade.operate(index, price, amount);
            // Refresh is needed every time a new trade is added, has to recheck the currentTrade
            openedTrades.add(operatedTrade);
            refreshTrade();
            this.action = StrategyAction.ENTER;
            return true;
        }
        return false;
    }

    @Override
    public boolean buildOn(int index, Decimal price, Decimal amount) {
        operatedTrade = getCurrentTrade();
        Order newOrder;
        checkOperatedTradeState(operatedTrade);
        if (operatedTrade.isOpened()) {
            Trade builtOnTrade = new Trade(this.startingType);
            newOrder = builtOnTrade.operate(index, price, amount);
            openedTrades.add(builtOnTrade);
            refreshTrade();
            this.action = StrategyAction.BUILDON;
            return true;
        }
        return false;
    }

    @Override
    public boolean exit(int index, Decimal price, Decimal amount) {
        operatedTrade = getCurrentTrade();
        Order newOrder;
        checkOperatedTradeState(operatedTrade);
        if (operatedTrade.isOpened()) {
//            Should be enabled, when Trade.equals() method is corrected,
//            otherwise throws NPE
//            if (!openedTrades.contains(operatedTrade)) {
//                throw new IllegalArgumentException ("Current trade is not contained in opened Trades");
//            }
            Decimal entryAmount = operatedTrade.getEntry().getAmount();
            closedTrades = new LinkedList<>();
            if (entryAmount.equals(amount)) {
                newOrder = operatedTrade.operate(index, price, amount);
                openedTrades.remove(operatedTrade);
                refreshTrade();
                closedTrades.add(operatedTrade);
            } else if (entryAmount.isGreaterThan(amount)) {
                Order entryOrder = operatedTrade.getEntry();
                openedTrades.remove(operatedTrade);
                Order newEntryOrder = createOrder(entryOrder.getType(),
                        entryOrder.getIndex(),
                        entryOrder.getPrice(),
                        amount);
                Order newExitOrder = createOrder(entryOrder.getType().complementType(), index, price, amount);
                operatedTrade = new Trade(newEntryOrder, newExitOrder);
                Trade remainTrade = new Trade(entryOrder.getType());
                remainTrade.operate(entryOrder.getIndex(), entryOrder.getPrice(), entryAmount.minus(amount));
                openedTrades.add(remainTrade);
                refreshTrade();
                closedTrades.add(operatedTrade);
            } else if (entryAmount.isLessThan(amount)) {
                while (amount.isGreaterThan(Decimal.ZERO) && operatedTrade.getEntry() != null) {
                    entryAmount = operatedTrade.getEntry().getAmount();
                    if (amount.isGreaterThan(entryAmount)) {
                        amount = amount.minus(entryAmount);
                        newOrder = operatedTrade.operate(index, price, entryAmount);
                    } else {
                        newOrder = operatedTrade.operate(index, price, amount);
                    }
                    openedTrades.remove(operatedTrade);
                    refreshTrade();
                    closedTrades.add(operatedTrade);
                    operatedTrade = getCurrentTrade();
                }
            }
            this.action = StrategyAction.EXIT;
            return true;
        }
        return false;
    }

    @Override
    public Trade getOpenTradeToRecord() {
        if (!action.equals(StrategyAction.ENTER) && !action.equals(StrategyAction.BUILDON)) {
            throw new IllegalStateException("TradingManager state should be ENTER or BUILDON");
        }
        this.action = StrategyAction.NOTHING;
        return operatedTrade;
    }

    @Override
    public List<Trade> getClosedTradesToRecord() {
        if (!action.equals(StrategyAction.EXIT)) {
            throw new IllegalStateException("TradingManager state should be EXIT");
        }
        this.action = StrategyAction.NOTHING;
        return closedTrades;
    }

    @Override
    public StrategyAction getAction() {
        return action;
    }

    private void checkOperatedTradeState(Trade operatedTrade) {
        if (operatedTrade.isClosed()) {
            // Operated trade closed, should not occur at any point
            throw new IllegalStateException("Operated trade should not be closed");
        }
    }
    private class Fifo implements Comparator<Trade> {
        @Override
        public int compare(Trade o1, Trade o2) {
            return Integer.compare(o1.getEntry().getIndex(), o2.getEntry().getIndex());
        }

    }

    private Order createOrder(Order.OrderType type, int index, Decimal price, Decimal amount) {
        Order createdOrder = null;
        if (type.equals(Order.OrderType.BUY)) {
            createdOrder = Order.buyAt(index, price, amount);
        } else if (type.equals(Order.OrderType.SELL)) {
            createdOrder = Order.sellAt(index, price, amount);
        }
        return createdOrder;
    }
}
