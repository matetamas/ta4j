package org.ta4j.core.buildon;

import org.junit.Test;
import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class BaseTradingManagerTest {

    private Trade trade1, trade2;

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
    public void emptyRecordStatesTest() {
        TradingManager emptyRecord = new BaseTradingManager();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, emptyRecord.getAction());
        try {
            trade1 = emptyRecord.getOpenTradeToRecord();
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        try {
            trade2 = emptyRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void openedRecordTest() {
        TradingManager openedRecord = new BaseTradingManager();
        assertEquals(0, openedRecord.getOpenedTrades().size());
        assertNull(openedRecord.getCurrentTrade().getEntry());
        assertNull(openedRecord.getCurrentTrade().getExit());

        assertNotNull(openedRecord.getCurrentTrade());
        assertTrue(openedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        trade1 = openedRecord.getOpenTradeToRecord();
        assertEquals(1, openedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), openedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(openedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(openedRecord.getCurrentTrade());
        assertTrue(openedRecord.exit(3, Decimal.NaN, Decimal.NaN));
        trade2 = openedRecord.getClosedTradesToRecord().get(0);
        assertEquals(0, openedRecord.getOpenedTrades().size());
        assertNull(openedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade2.getEntry());
        assertNull(openedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), trade2.getExit());

        assertNotNull(openedRecord.getCurrentTrade());
        assertTrue(openedRecord.enter(7, Decimal.NaN, Decimal.NaN));
        trade1 = openedRecord.getOpenTradeToRecord();
        assertEquals(1, openedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), openedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(openedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(openedRecord.getCurrentTrade().isOpened());
        assertFalse(openedRecord.isClosed());
    }

    @Test
    public void openedRecordStatesTest() {
        TradingManager openedRecord = new BaseTradingManager();

        assertTrue(openedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.ENTER, openedRecord.getAction());
        trade1 = openedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, openedRecord.getAction());
        try {
            trade2 = openedRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }

        assertTrue(openedRecord.exit(3, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.EXIT, openedRecord.getAction());
        try {
            trade1 = openedRecord.getOpenTradeToRecord();
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        trade2 = openedRecord.getClosedTradesToRecord().get(0);
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, openedRecord.getAction());

        assertTrue(openedRecord.enter(7, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.ENTER, openedRecord.getAction());
        trade1 = openedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, openedRecord.getAction());
        try {
            trade2 = openedRecord.getClosedTradesToRecord().get(0);
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void closedRecordTest() {
        TradingManager closedRecord = new BaseTradingManager();
        assertEquals(0, closedRecord.getOpenedTrades().size());
        assertNull(closedRecord.getCurrentTrade().getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());

        assertNotNull(closedRecord.getCurrentTrade());
        assertTrue(closedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        trade1 = closedRecord.getOpenTradeToRecord();
        assertEquals(1, closedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(closedRecord.getCurrentTrade());
        assertTrue(closedRecord.exit(3, Decimal.NaN, Decimal.NaN));
        trade2 = closedRecord.getClosedTradesToRecord().get(0);
        assertEquals(0, closedRecord.getOpenedTrades().size());
        assertNull(closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade2.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), trade2.getExit());

        assertNotNull(closedRecord.getCurrentTrade());
        assertTrue(closedRecord.enter(7, Decimal.NaN, Decimal.NaN));
        trade1 = closedRecord.getOpenTradeToRecord();
        assertEquals(1, closedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(closedRecord.getCurrentTrade());
        assertTrue(closedRecord.exit(8, Decimal.NaN, Decimal.NaN));
        trade2 = closedRecord.getClosedTradesToRecord().get(0);
        assertEquals(0, closedRecord.getOpenedTrades().size());
        assertNull(closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), trade2.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(8, Decimal.NaN, Decimal.NaN), trade2.getExit());

        assertTrue(closedRecord.getCurrentTrade().isNew());
        assertTrue(closedRecord.isClosed());
    }

    @Test
    public void closedRecordStatesTest() {
        TradingManager closedRecord = new BaseTradingManager();

        assertTrue(closedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        trade1 = closedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, closedRecord.getAction());
        try {
            trade2 = closedRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }

        assertTrue(closedRecord.exit(3, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.EXIT, closedRecord.getAction());
        try {
            trade1 = closedRecord.getOpenTradeToRecord();
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        trade2 = closedRecord.getClosedTradesToRecord().get(0);
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, closedRecord.getAction());

        assertTrue(closedRecord.enter(7, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.ENTER, closedRecord.getAction());
        trade1 = closedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, closedRecord.getAction());
        try {
            trade2 = closedRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }

        assertTrue(closedRecord.exit(8, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.EXIT, closedRecord.getAction());
        try {
            trade1 = closedRecord.getOpenTradeToRecord();
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
        trade2 = closedRecord.getClosedTradesToRecord().get(0);
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, closedRecord.getAction());
    }

    @Test
    public void doubleOpenedRecordTest() {
        TradingManager doubleOpenedRecord = new BaseTradingManager();
        assertEquals(0, doubleOpenedRecord.getOpenedTrades().size());
        assertNull(doubleOpenedRecord.getCurrentTrade().getEntry());
        assertNull(doubleOpenedRecord.getCurrentTrade().getExit());

        assertNotNull(doubleOpenedRecord.getCurrentTrade());
        assertTrue(doubleOpenedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        trade1 = doubleOpenedRecord.getOpenTradeToRecord();
        assertEquals(1, doubleOpenedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(doubleOpenedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(doubleOpenedRecord.getCurrentTrade());
        assertTrue(doubleOpenedRecord.buildOn(9, Decimal.NaN, Decimal.NaN));
        trade1 = doubleOpenedRecord.getOpenTradeToRecord();
        assertEquals(2, doubleOpenedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(9, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(doubleOpenedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(doubleOpenedRecord.getCurrentTrade().isOpened());
        assertFalse(doubleOpenedRecord.isClosed());
    }

    @Test
    public void doubleOpenedRecordStatesTest() {
        TradingManager doubleOpenedRecord = new BaseTradingManager();

        assertTrue(doubleOpenedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.ENTER, doubleOpenedRecord.getAction());
        trade1 = doubleOpenedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, doubleOpenedRecord.getAction());
        try {
            trade2 = doubleOpenedRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }

        assertTrue(doubleOpenedRecord.buildOn(9, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.BUILDON, doubleOpenedRecord.getAction());
        trade1 = doubleOpenedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, doubleOpenedRecord.getAction());
        try {
            trade2 = doubleOpenedRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void doubleClosedRecordTest() {
        TradingManager doubleClosedRecord = new BaseTradingManager(Order.OrderType.SELL);
        assertEquals(0, doubleClosedRecord.getOpenedTrades().size());
        assertNull(doubleClosedRecord.getCurrentTrade().getEntry());
        assertNull(doubleClosedRecord.getCurrentTrade().getExit());

        assertNotNull(doubleClosedRecord.getCurrentTrade());
        assertTrue(doubleClosedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        trade1 = doubleClosedRecord.getOpenTradeToRecord();
        assertEquals(1, doubleClosedRecord.getOpenedTrades().size());
        assertEquals(Order.sellAt(0, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.sellAt(0, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(doubleClosedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(doubleClosedRecord.getCurrentTrade());
        assertTrue(doubleClosedRecord.buildOn(10, Decimal.NaN, Decimal.NaN));
        trade1 = doubleClosedRecord.getOpenTradeToRecord();
        assertEquals(2, doubleClosedRecord.getOpenedTrades().size());
        assertEquals(Order.sellAt(0, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.sellAt(10, Decimal.NaN, Decimal.NaN), trade1.getEntry());
        assertNull(doubleClosedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(doubleClosedRecord.getCurrentTrade().isOpened());
        assertFalse(doubleClosedRecord.isClosed());
    }

    @Test
    public void doubleClosedRecordStatesTest() {
        TradingManager doubleClosedRecord = new BaseTradingManager(Order.OrderType.SELL);

        assertTrue(doubleClosedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.ENTER, doubleClosedRecord.getAction());
        trade1 = doubleClosedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, doubleClosedRecord.getAction());
        try {
            trade2 = doubleClosedRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }

        assertTrue(doubleClosedRecord.buildOn(10, Decimal.NaN, Decimal.NaN));
        assertEquals(BaseStrategyBuildOn.StrategyAction.BUILDON, doubleClosedRecord.getAction());
        trade1 = doubleClosedRecord.getOpenTradeToRecord();
        assertEquals(BaseStrategyBuildOn.StrategyAction.NOTHING, doubleClosedRecord.getAction());
        try {
            trade2 = doubleClosedRecord.getClosedTradesToRecord().get(0);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void nullEntryOrderTypeException() {
        try {
            BaseTradingManager bm = new BaseTradingManager(null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    @Test
    public void enterAfterEnterException() {
        TradingManager doubleEnterRecord = new BaseTradingManager();
        assertTrue(doubleEnterRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertFalse(doubleEnterRecord.enter(10, Decimal.NaN, Decimal.NaN));
    }

    @Test
    public void buildOnException() {
        TradingManager buildOnRecord = new BaseTradingManager();
        assertFalse(buildOnRecord.buildOn(10, Decimal.NaN, Decimal.NaN));
    }

    @Test
    public void exitException() {
        TradingManager exitRecord = new BaseTradingManager();
        assertFalse(exitRecord.exit(10, Decimal.NaN, Decimal.NaN));
    }

    @Test
    public void enterAfterEnterUseInterfaceMethodException() {
        TradingManager doubleEnterRecord = new BaseTradingManager();
        doubleEnterRecord.enter(0);
        assertFalse(doubleEnterRecord.enter(10));
    }

    @Test
    public void buildOnUseInterfaceMethodException() {
        TradingManager buildOnRecord = new BaseTradingManager();
        assertFalse(buildOnRecord.buildOn(10));
    }

    @Test
    public void exitUseInterfaceMethodException() {
        TradingManager exitRecord = new BaseTradingManager();
        assertFalse(exitRecord.exit(10));
    }

    @Test
    public void closedRecordSmallerSellAmountTest() {
        TradingManager closedRecord = new BaseTradingManager();

        assertNotNull(closedRecord.getCurrentTrade());
        assertTrue(closedRecord.enter(0, Decimal.NaN, Decimal.THREE));
        trade1 = closedRecord.getOpenTradeToRecord();
        assertEquals(1, closedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.THREE), closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.THREE), trade1.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(closedRecord.exit(3, Decimal.NaN, Decimal.ONE));
        assertNotNull(closedRecord.getCurrentTrade());
        trade2 = closedRecord.getClosedTradesToRecord().get(0);
        assertEquals(1, closedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade2.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade2.getExit());

        trade1 = closedRecord.getOpenedTrades().peek();
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), trade1.getEntry());
        assertNull(trade1.getExit());
    }

    @Test
    public void closedRecordGreaterSellAmountTest() {
        TradingManager closedRecord = new BaseTradingManager();

        assertNotNull(closedRecord.getCurrentTrade());
        assertTrue(closedRecord.enter(0, Decimal.NaN, Decimal.ONE));
        trade1 = closedRecord.getOpenTradeToRecord();
        assertEquals(1, closedRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(closedRecord.exit(3, Decimal.NaN, Decimal.THREE));
        assertNotNull(closedRecord.getCurrentTrade());
        trade2 = closedRecord.getClosedTradesToRecord().get(0);
        assertEquals(0, closedRecord.getOpenedTrades().size());
        assertNull(closedRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade2.getEntry());
        assertNull(closedRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade2.getExit());
    }

    @Test
    public void closedRecord_Enter1_BuildOn2_Exit3_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.ONE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.buildOn(1, Decimal.NaN, Decimal.TWO));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.TWO), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.THREE));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(0, difficultRecord.getOpenedTrades().size());
        assertNull(difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.TWO), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.TWO), trade2.getExit());
    }

    @Test
    public void closedRecord_Enter1_BuildOn1_Exit3_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.ONE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.buildOn(1, Decimal.NaN, Decimal.ONE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.THREE));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(0, difficultRecord.getOpenedTrades().size());
        assertNull(difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade2.getExit());
    }

    @Test
    public void closedRecord_Enter1_BuildOn3_Exit3_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.ONE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.buildOn(1, Decimal.NaN, Decimal.THREE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.THREE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.THREE));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.TWO), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.TWO), trade2.getExit());
    }

    @Test
    public void closedRecord_Enter1_BuildOn3_Exit3_Exit1_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.ONE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.buildOn(1, Decimal.NaN, Decimal.THREE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.THREE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.THREE));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.TWO), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.TWO), trade2.getExit());

        assertTrue(difficultRecord.exit(4, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(1, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        assertEquals(0, difficultRecord.getOpenedTrades().size());
        assertNull(difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(4, Decimal.NaN, Decimal.ONE), trade1.getExit());
    }

    @Test
    public void closedRecord_Enter1_BuildOn3_Exit2_Exit1_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.ONE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.buildOn(1, Decimal.NaN, Decimal.THREE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.THREE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.TWO));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.TWO), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade2.getExit());

        assertTrue(difficultRecord.exit(4, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(1, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(4, Decimal.NaN, Decimal.ONE), trade1.getExit());
    }

    @Test
    public void closedRecord_Enter1_BuildOn3_Exit2_Exit3_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.ONE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.buildOn(1, Decimal.NaN, Decimal.THREE));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.THREE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.TWO));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.TWO), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.ONE), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade2.getExit());

        assertTrue(difficultRecord.exit(4, Decimal.NaN, Decimal.THREE));
        assertNotNull(difficultRecord.getCurrentTrade());
        closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(1, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        assertEquals(0, difficultRecord.getOpenedTrades().size());
        assertNull(difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.TWO), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(4, Decimal.NaN, Decimal.TWO), trade1.getExit());
    }

    @Test
    public void closedRecord_Enter2_Exit1_BuildOn1_Exit2_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.TWO));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(1, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(1, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(1, Decimal.NaN, Decimal.ONE), trade1.getExit());

        assertTrue(difficultRecord.buildOn(2, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(2, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.TWO));
        assertNotNull(difficultRecord.getCurrentTrade());
        closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(0, difficultRecord.getOpenedTrades().size());
        assertNull(difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(2, Decimal.NaN, Decimal.ONE), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade2.getExit());
    }

    @Test
    public void closedRecord_Enter2_Exit1_BuildOn1_Exit1_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.TWO));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(1, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(1, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(1, Decimal.NaN, Decimal.ONE), trade1.getExit());

        assertTrue(difficultRecord.buildOn(2, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(2, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(1, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(2, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
    }

    @Test
    public void closedRecord_Enter2_Exit1_BuildOn1_Exit3_AmountTest() {
        TradingManager difficultRecord = new BaseTradingManager();

        assertNotNull(difficultRecord.getCurrentTrade());
        assertTrue(difficultRecord.enter(0, Decimal.NaN, Decimal.TWO));
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.TWO), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(1, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        List<Trade> closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(1, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        assertEquals(1, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(1, Decimal.NaN, Decimal.ONE), trade1.getExit());

        assertTrue(difficultRecord.buildOn(2, Decimal.NaN, Decimal.ONE));
        assertNotNull(difficultRecord.getCurrentTrade());
        trade1 = difficultRecord.getOpenTradeToRecord();
        assertEquals(2, difficultRecord.getOpenedTrades().size());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(2, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertNull(trade1.getExit());

        assertTrue(difficultRecord.exit(3, Decimal.NaN, Decimal.THREE));
        assertNotNull(difficultRecord.getCurrentTrade());
        closedTradesToRecord = difficultRecord.getClosedTradesToRecord();
        assertEquals(2, closedTradesToRecord.size());
        trade1 = closedTradesToRecord.get(0);
        trade2 = closedTradesToRecord.get(1);
        assertEquals(0, difficultRecord.getOpenedTrades().size());
        assertNull(difficultRecord.getCurrentTrade().getEntry());
        assertEquals(Order.buyAt(0, Decimal.NaN, Decimal.ONE), trade1.getEntry());
        assertEquals(Order.buyAt(2, Decimal.NaN, Decimal.ONE), trade2.getEntry());
        assertNull(difficultRecord.getCurrentTrade().getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade1.getExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.ONE), trade2.getExit());
    }
}