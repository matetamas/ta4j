package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Trade;

import java.util.PriorityQueue;

public interface TradingManager {
    /**
     * @return the current trade
     */
    Trade getCurrentTrade();

    /**
     * @return the opened trades list
     */
    PriorityQueue<Trade> getOpenedTrades();

    /**
     * Operates an order in the trading record.
     * @param index the index to operate the order
     */
    default Trade operate(int index) {
        return operate(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an order in the trading record.
     * @param index the index to operate the order
     * @param price the price of the order
     * @param amount the amount to be ordered
     */
    Trade operate(int index, Decimal price, Decimal amount);

    /**
     * Operates an entry order in the trading record.
     * @param index the index to operate the entry
     * @return true if the entry has been operated, false otherwise
     */
    default Trade enter(int index) {
        return enter(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an entry order in the trading record.
     * @param index the index to operate the entry
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the entry has been operated, false otherwise
     */
    Trade enter(int index, Decimal price, Decimal amount);

    /**
     * Operates a build on order in the trading record.
     * @param index the index to operate the entry
     * @return true if the build on has been operated, false otherwise
     */
    default Trade buildOn(int index) {
        return buildOn(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates a build on order in the trading record.
     * @param index the index to operate the entry
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the build on has been operated, false otherwise
     */
    Trade buildOn(int index, Decimal price, Decimal amount);

    /**
     * Operates an exit order in the trading record.
     * @param index the index to operate the exit
     * @return true if the exit has been operated, false otherwise
     */
    default Trade exit(int index) {
        return exit(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an exit order in the trading record.
     * @param index the index to operate the exit
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the exit has been operated, false otherwise
     */
    Trade exit(int index, Decimal price, Decimal amount);

    /**
     * @return true if no trade is open, false otherwise
     */
    default boolean isClosed() {
        return !getCurrentTrade().isOpened();
    }
}
