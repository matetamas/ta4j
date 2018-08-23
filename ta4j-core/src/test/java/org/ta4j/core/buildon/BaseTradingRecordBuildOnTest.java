package org.ta4j.core.buildon;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.*;

import static org.junit.Assert.*;

public class BaseTradingRecordBuildOnTest {

    private TradingRecordBuildOn emptyRecord, openedRecord, closedRecord,
            doubleOpenedRecord, doubleClosedRecord;

    @Before
    @Test
    public void setUp() {
        emptyRecord = new BaseTradingRecordBuildOn();

        openedRecord = new BaseTradingRecordBuildOn();
        assertTrue(openedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertTrue(openedRecord.exit(3, Decimal.NaN, Decimal.NaN));
        assertTrue(openedRecord.enter(7, Decimal.NaN, Decimal.NaN));

        closedRecord = new BaseTradingRecordBuildOn();
        assertTrue(closedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertTrue(closedRecord.exit(3, Decimal.NaN, Decimal.NaN));
        assertTrue(closedRecord.enter(7, Decimal.NaN, Decimal.NaN));
        assertTrue(closedRecord.exit(8, Decimal.NaN, Decimal.NaN));

        doubleOpenedRecord = new BaseTradingRecordBuildOn();
        assertTrue(doubleOpenedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertTrue(doubleOpenedRecord.buildOn(9, Decimal.NaN, Decimal.NaN));

        doubleClosedRecord = new BaseTradingRecordBuildOn(Order.OrderType.SELL);
        assertTrue(doubleClosedRecord.enter(0, Decimal.NaN, Decimal.NaN));
        assertTrue(doubleClosedRecord.buildOn(10, Decimal.NaN, Decimal.NaN));
    }

    @Test
    public void getCurrentTrade() {
        assertTrue(emptyRecord.getCurrentTrade().isNew());
        assertTrue(openedRecord.getCurrentTrade().isOpened());
        assertTrue(closedRecord.getCurrentTrade().isNew());
        assertTrue(doubleOpenedRecord.getCurrentTrade().isOpened());
        assertTrue(doubleClosedRecord.getCurrentTrade().isOpened());
    }

    @Test
    public void operate() {
        TradingRecord record = new BaseTradingRecord();

        record.operate(1);
        assertTrue(record.getCurrentTrade().isOpened());
        assertEquals(0, record.getTradeCount());
        assertNull(record.getLastTrade());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.NaN), record.getLastOrder());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.NaN), record.getLastOrder(Order.OrderType.BUY));
        assertNull(record.getLastOrder(Order.OrderType.SELL));
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.NaN), record.getLastEntry());
        assertNull(record.getLastExit());

        record.operate(3);
        assertTrue(record.getCurrentTrade().isNew());
        assertEquals(1, record.getTradeCount());
        assertEquals(new Trade(Order.buyAt(1, Decimal.NaN, Decimal.NaN), Order.sellAt(3, Decimal.NaN, Decimal.NaN)), record.getLastTrade());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), record.getLastOrder());
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.NaN), record.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), record.getLastOrder(Order.OrderType.SELL));
        assertEquals(Order.buyAt(1, Decimal.NaN, Decimal.NaN), record.getLastEntry());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), record.getLastExit());

        record.operate(5);
        assertTrue(record.getCurrentTrade().isOpened());
        assertEquals(1, record.getTradeCount());
        assertEquals(new Trade(Order.buyAt(1, Decimal.NaN, Decimal.NaN), Order.sellAt(3, Decimal.NaN, Decimal.NaN)), record.getLastTrade());
        assertEquals(Order.buyAt(5, Decimal.NaN, Decimal.NaN), record.getLastOrder());
        assertEquals(Order.buyAt(5, Decimal.NaN, Decimal.NaN), record.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), record.getLastOrder(Order.OrderType.SELL));
        assertEquals(Order.buyAt(5, Decimal.NaN, Decimal.NaN), record.getLastEntry());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), record.getLastExit());
    }

    @Test
    public void isClosed() {
        assertTrue(emptyRecord.isClosed());
        assertFalse(openedRecord.isClosed());
        assertTrue(closedRecord.isClosed());
        assertFalse(doubleOpenedRecord.isClosed());
        assertFalse(doubleClosedRecord.isClosed());
    }

    @Test
    public void getTradeCount() {
        assertEquals(0, emptyRecord.getTradeCount());
        assertEquals(1, openedRecord.getTradeCount());
        assertEquals(2, closedRecord.getTradeCount());
        assertEquals(0, doubleOpenedRecord.getTradeCount());
        assertEquals(0, doubleClosedRecord.getTradeCount());
    }

    @Test
    public void getLastTrade() {
        assertNull(emptyRecord.getLastTrade());
        assertEquals(new Trade(Order.buyAt(0, Decimal.NaN, Decimal.NaN), Order.sellAt(3, Decimal.NaN, Decimal.NaN)), openedRecord.getLastTrade());
        assertEquals(new Trade(Order.buyAt(7, Decimal.NaN, Decimal.NaN), Order.sellAt(8, Decimal.NaN, Decimal.NaN)), closedRecord.getLastTrade());
        assertNull(doubleOpenedRecord.getLastTrade());
        assertNull(doubleClosedRecord.getLastTrade());
    }

    @Test
    public void getLastOrder() {
        // Last order
        assertNull(emptyRecord.getLastOrder());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), openedRecord.getLastOrder());
        assertEquals(Order.sellAt(8, Decimal.NaN, Decimal.NaN), closedRecord.getLastOrder());
        assertEquals(Order.buyAt(9, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getLastOrder());
        assertEquals(Order.sellAt(10, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getLastOrder());
        // Last BUY order
        assertNull(emptyRecord.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), openedRecord.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), closedRecord.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.buyAt(9, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getLastOrder(Order.OrderType.BUY));
        assertNull(doubleClosedRecord.getLastOrder(Order.OrderType.BUY));
        // Last SELL order
        assertNull(emptyRecord.getLastOrder(Order.OrderType.SELL));
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), openedRecord.getLastOrder(Order.OrderType.SELL));
        assertEquals(Order.sellAt(8, Decimal.NaN, Decimal.NaN), closedRecord.getLastOrder(Order.OrderType.SELL));
        assertNull(doubleOpenedRecord.getLastOrder(Order.OrderType.SELL));
        assertEquals(Order.sellAt(10, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getLastOrder(Order.OrderType.SELL));
    }

    @Test
    public void getLastEntryExit() {
        // Last entry
        assertNull(emptyRecord.getLastEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), openedRecord.getLastEntry());
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), closedRecord.getLastEntry());
        assertEquals(Order.buyAt(9, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getLastEntry());
        assertEquals(Order.sellAt(10, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getLastEntry());
        // Last exit
        assertNull(emptyRecord.getLastExit());
        assertEquals(Order.sellAt(3, Decimal.NaN, Decimal.NaN), openedRecord.getLastExit());
        assertEquals(Order.sellAt(8, Decimal.NaN, Decimal.NaN), closedRecord.getLastExit());
        assertNull(doubleOpenedRecord.getLastExit());
        assertNull(doubleClosedRecord.getLastExit());
    }

//    @Test
//    public void test() {
//        Comparator<Trade> fifo = Comparator.comparingInt(o -> o.getEntry().getIndex());
//        PriorityQueue<Trade> trades = new PriorityQueue<>(fifo);
//        Trade trade1 = new Trade(
//                Order.buyAt(7, Decimal.valueOf(2), Decimal.valueOf(3)),
//                Order.sellAt(4, Decimal.valueOf(5), Decimal.valueOf(6))
//        );
//        Trade trade2 = new Trade(
//                Order.buyAt(6, Decimal.valueOf(7), Decimal.valueOf(8)),
//                Order.sellAt(9, Decimal.valueOf(10), Decimal.valueOf(12))
//        );
//        Trade trade3 = new Trade(
//                Order.buyAt(13, Decimal.valueOf(14), Decimal.valueOf(15)),
//                Order.sellAt(16, Decimal.valueOf(17), Decimal.valueOf(18))
//        );
//        trades.add(trade1);
//        trades.add(trade2);
//        trades.add(trade3);
//
//        for (Trade trade : trades) {
//            System.out.println(trade.toString());
//        }
//        System.out.println();
//
//        Trade trade4 = new Trade(
//                Order.buyAt(1, Decimal.valueOf(14), Decimal.valueOf(15)),
//                Order.sellAt(16, Decimal.valueOf(17), Decimal.valueOf(18))
//        );
//        trades.add(trade4);
//        for (Trade trade : trades) {
//            System.out.println(trade.toString());
//        }
//        System.out.println();
//        Trade trade5 = trades.peek();
//        System.out.println(trade5);
//    }
//
//    @Test
//    public void test2() {
//        PriorityQueue<Trade> trades = new PriorityQueue<>();
//        Trade trade5 = trades.peek();
//        System.out.println(trade5);
//    }
}