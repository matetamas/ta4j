package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Order.OrderType;
import org.ta4j.core.Trade;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static org.ta4j.core.buildon.BaseStrategyBuildOn.*;

public class BaseTradingManager implements TradingManager {

    /** The entry type (BUY or SELL) in the trading session */
    protected OrderType startingType;

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
    public BaseTradingManager() {this(OrderType.BUY);}

    /**
     * Constructor.
     *
     * @param entryOrderType the {@link OrderType order type} of entries in the trading session
     */
    public BaseTradingManager(OrderType entryOrderType) {
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

    // Refresh is needed every time a trade is added or removed, has to recheck the currentTrade
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
            operatedTrade = builtOnTrade;
            this.action = StrategyAction.BUILDON;
            return true;
        }
        return false;
    }

    @Override
    public boolean exit(int index, Decimal price, Decimal exitAmount) {
        Order newOrder;
        Trade operatedExitTrade = getCurrentTrade();
        checkOperatedTradeState(operatedExitTrade);
//      Should be enabled, when Trade.equals() method is corrected,
//      otherwise throws NPE
//      if (!openedTrades.contains(operatedExitTrade)) {
//          throw new IllegalArgumentException ("Current trade is not contained in opened Trades");
//      }
        if (operatedExitTrade.isNew()) return false;
        while (!openedTrades.isEmpty() && !exitAmount.equals(Decimal.ZERO)) {
            closedTrades = new LinkedList<>();
            do {
                Order entryOrder = operatedExitTrade.getEntry();
                Decimal entryAmount = entryOrder.getAmount();
                openedTrades.remove(operatedExitTrade);
                refreshTrade();
                if (entryAmount.equals(exitAmount)) {
                    newOrder = operatedExitTrade.operate(index, price, exitAmount);
                    exitAmount = Decimal.ZERO;
                    closedTrades.add(operatedExitTrade);
                } else if (entryAmount.isGreaterThan(exitAmount)) {
                    OrderType entryType = entryOrder.getType();
                    int entryIndex = entryOrder.getIndex();
                    Decimal entryPrice = entryOrder.getPrice();
                    Order newEntryOrder = createOrder(entryType, entryIndex, entryPrice, exitAmount);
                    Order newExitOrder = createOrder(entryType.complementType(), index, price, exitAmount);
                    operatedExitTrade = new Trade(newEntryOrder, newExitOrder);
                    Trade remainTrade = new Trade(entryType);
                    remainTrade.operate(entryIndex, entryPrice, entryAmount.minus(exitAmount));
                    exitAmount = Decimal.ZERO;
                    openedTrades.add(remainTrade);
                    refreshTrade();
                    closedTrades.add(operatedExitTrade);
                } else if (entryAmount.isLessThan(exitAmount)) {
                    exitAmount = exitAmount.minus(entryAmount);
                    newOrder = operatedExitTrade.operate(index, price, entryAmount);
                    closedTrades.add(operatedExitTrade);
                    operatedExitTrade = getCurrentTrade();
                }
            } while (operatedExitTrade.isOpened());
        }
        this.action = StrategyAction.EXIT;
        return true;
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

    private Order createOrder(OrderType type, int index, Decimal price, Decimal amount) {
        Order createdOrder = null;
        if (type.equals(OrderType.BUY)) {
            createdOrder = Order.buyAt(index, price, amount);
        } else if (type.equals(OrderType.SELL)) {
            createdOrder = Order.sellAt(index, price, amount);
        }
        return createdOrder;
    }
}
