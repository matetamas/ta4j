package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Trade;

public interface TradingManager {
    /**
     * @return the current trade
     */
    Trade getCurrentTrade();

    /**
     * Operates an order in the trading record.
     * @param index the index to operate the order
     */
    default void operate(int index) {
        operate(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an order in the trading record.
     * @param index the index to operate the order
     * @param price the price of the order
     * @param amount the amount to be ordered
     */
    void operate(int index, Decimal price, Decimal amount);

    /**
     * Operates an entry order in the trading record.
     * @param index the index to operate the entry
     * @return true if the entry has been operated, false otherwise
     */
    default boolean enter(int index) {
        return enter(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an entry order in the trading record.
     * @param index the index to operate the entry
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the entry has been operated, false otherwise
     */
    boolean enter(int index, Decimal price, Decimal amount);

    /**
     * Operates a build on order in the trading record.
     * @param index the index to operate the entry
     * @return true if the build on has been operated, false otherwise
     */
    default boolean buildOn(int index) {
        return buildOn(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates a build on order in the trading record.
     * @param index the index to operate the entry
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the build on has been operated, false otherwise
     */
    boolean buildOn(int index, Decimal price, Decimal amount);

    /**
     * Operates an exit order in the trading record.
     * @param index the index to operate the exit
     * @return true if the exit has been operated, false otherwise
     */
    default boolean exit(int index) {
        return exit(index, Decimal.NaN, Decimal.NaN);
    }

    /**
     * Operates an exit order in the trading record.
     * @param index the index to operate the exit
     * @param price the price of the order
     * @param amount the amount to be ordered
     * @return true if the exit has been operated, false otherwise
     */
    boolean exit(int index, Decimal price, Decimal amount);

    /**
     * @return true if no trade is open, false otherwise
     */
    default boolean isClosed() {
        return !getCurrentTrade().isOpened();
    }
}
