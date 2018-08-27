package org.ta4j.core.buildon;

import org.junit.Test;
import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import static org.junit.Assert.*;

public class BaseTradingManagerTest {

    private Trade trade1, trade2, trade3;

    @Test
    public void emptyRecordTest() {
        TradingManager emptyRecord = new BaseTradingManager();
        assertEquals(0, emptyRecord.getOpenedTrades().size());
        assertNull(emptyRecord.getCurrentTrade().getEntry());
        assertNull(emptyRecord.getCurrentTrade().getExit());

        assertTrue(emptyRecord.getCurrentTrade().isNew());
        assertTrue(emptyRecord.isClosed());
    }

    @Test
    public void openedRecordTest() {
        TradingManager openedRecord = new BaseTradingManager();
        assertEquals(0, openedRecord.getOpenedTrades().size());

        trade1 = openedRecord.enter(0, Decimal.NaN, Decimal.NaN);
        assertEquals(1, openedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), openedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(openedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        trade2 = openedRecord.exit(3, Decimal.NaN, Decimal.NaN);
        assertEquals(0, openedRecord.getOpenedTrades().size());
        assertNull(openedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade2.getEntry());
        assertNull(openedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), trade2.getExit());

        trade3 = openedRecord.enter(7, Decimal.NaN, Decimal.NaN);
        assertEquals(1, openedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), openedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), trade3.getEntry());
        assertNull(openedRecord.getCurrentTrade().getExit());
        assertNull(trade3.getExit());

        assertTrue(openedRecord.getCurrentTrade().isOpened());
        assertFalse(openedRecord.isClosed());
    }

    @Test
    public void closedRecordTest() {
        TradingManager closedRecord = new BaseTradingManager();
        assertEquals(0, closedRecord.getOpenedTrades().size());
        assertNull(closedRecord.getCurrentTrade().getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());

        trade1 = closedRecord.enter(0, Decimal.NaN, Decimal.NaN);
        assertEquals(1, closedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        trade2 = closedRecord.exit(3, Decimal.NaN, Decimal.NaN);
        assertEquals(0, closedRecord.getOpenedTrades().size());
        assertNull(closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade2.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), trade2.getExit());

        trade3 = closedRecord.enter(7, Decimal.NaN, Decimal.NaN);
        assertEquals(1, closedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), trade3.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertNull(trade3.getExit());

        Trade trade4 = closedRecord.exit(8, Decimal.NaN, Decimal.NaN);
        assertEquals(0, closedRecord.getOpenedTrades().size());
        assertNull(closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), trade4.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(8, Decimal.NaN, Decimal.NaN), trade4.getExit());

        assertTrue(closedRecord.getCurrentTrade().isNew());
        assertTrue(closedRecord.isClosed());
    }

    @Test
    public void doubleOpenedRecordTest() {
        TradingManager doubleOpenedRecord = new BaseTradingManager();
        assertEquals(0, doubleOpenedRecord.getOpenedTrades().size());
        assertNull(doubleOpenedRecord.getCurrentTrade().getEntry());
        assertNull(doubleOpenedRecord.getCurrentTrade().getExit());

        trade1 = doubleOpenedRecord.enter(0, Decimal.NaN, Decimal.NaN);
        assertEquals(1, doubleOpenedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(doubleOpenedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        trade2 = doubleOpenedRecord.buildOn(9, Decimal.NaN, Decimal.NaN);
        assertEquals(2, doubleOpenedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(9, Decimal.NaN, Decimal.NaN), trade2.getEntry());
        assertNull(doubleOpenedRecord.getCurrentTrade().getExit());
        assertNull(trade2.getExit());

        assertTrue(doubleOpenedRecord.getCurrentTrade().isOpened());
        assertFalse(doubleOpenedRecord.isClosed());
    }

    @Test
    public void doubleClosedRecordTest() {
        TradingManager doubleClosedRecord = new BaseTradingManager(Order.OrderType.SELL);
        assertEquals(0, doubleClosedRecord.getOpenedTrades().size());
        assertNull(doubleClosedRecord.getCurrentTrade().getEntry());
        assertNull(doubleClosedRecord.getCurrentTrade().getExit());

        trade1 = doubleClosedRecord.enter(0, Decimal.NaN, Decimal.NaN);
        assertEquals(1, doubleClosedRecord.getOpenedTrades().size());
        assertEquals(Order.sellAt(0, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.sellAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(doubleClosedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        trade2 = doubleClosedRecord.buildOn(10, Decimal.NaN, Decimal.NaN);
        assertEquals(2, doubleClosedRecord.getOpenedTrades().size());
        assertEquals(Order.sellAt(0, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.sellAt(10, Decimal.NaN, Decimal.NaN), trade2.getEntry());
        assertNull(doubleClosedRecord.getCurrentTrade().getExit());
        assertNull(trade2.getExit());

        assertTrue(doubleClosedRecord.getCurrentTrade().isOpened());
        assertFalse(doubleClosedRecord.isClosed());
    }
}