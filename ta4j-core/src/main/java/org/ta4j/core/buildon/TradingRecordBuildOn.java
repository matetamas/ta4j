package org.ta4j.core.buildon;

import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import java.util.List;

public interface TradingRecordBuildOn {
    /**
     * @return the recorded trades
     */
    List<Trade> getTrades();

    /**
     * @return the number of recorded trades
     */
    default int getTradeCount() {
        return getTrades().size();
    }

    /**
     * @return the last trade recorded
     */
    default Trade getLastTrade() {
        List<Trade> trades = getTrades();
        if (!trades.isEmpty()) {
            return trades.get(trades.size() - 1);
        }
        return null;
    }

    /**
     * @return the last order recorded
     */
    Order getLastOrder();
    /**
     * @param orderType the type of the order to get the last of
     * @return the last order (of the provided type) recorded
     */
    Order getLastOrder(Order.OrderType orderType);

    /**
     * @return the last entry order recorded
     */
    Order getLastEntry();

    /**
     * @return the last exit order recorded
     */
    Order getLastExit();

    void recordTrade(Trade trade);

    void recordListOfClosedTrades(List<Trade> closedTrades);
}
