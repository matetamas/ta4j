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
        Trade openTrade1 = new Trade();
        openTrade1.operate(0, Decimal.NaN, Decimal.NaN);

        Trade closeTrade1 = new Trade();
        closeTrade1.operate(0, Decimal.NaN, Decimal.NaN);
        closeTrade1.operate(3, Decimal.NaN, Decimal.NaN);

        Trade openTrade2 = new Trade();
        openTrade2.operate(7, Decimal.NaN, Decimal.NaN);

        Trade closeTrade2 = new Trade();
        closeTrade2.operate(7, Decimal.NaN, Decimal.NaN);
        closeTrade2.operate(8, Decimal.NaN, Decimal.NaN);

        Trade openSellTrade3 = new Trade(Order.OrderType.SELL);
        openSellTrade3.operate(9, Decimal.NaN, Decimal.NaN);

        Trade openSellTrade4 = new Trade(Order.OrderType.SELL);
        openSellTrade4.operate(10, Decimal.NaN, Decimal.NaN);

        emptyRecord = new BaseTradingRecordBuildOn();

        openedRecord = new BaseTradingRecordBuildOn();
        openedRecord.recordTrade(openTrade1);
        openedRecord.recordTrade(closeTrade1);
        openedRecord.recordTrade(openTrade2);

        closedRecord = new BaseTradingRecordBuildOn();
        closedRecord.recordTrade(openTrade1);
        closedRecord.recordTrade(closeTrade1);
        closedRecord.recordTrade(openTrade2);
        closedRecord.recordTrade(closeTrade2);

        doubleOpenedRecord = new BaseTradingRecordBuildOn();
        doubleOpenedRecord.recordTrade(openTrade1);
        doubleOpenedRecord.recordTrade(openTrade2);

        doubleClosedRecord = new BaseTradingRecordBuildOn(Order.OrderType.SELL);
        doubleClosedRecord.recordTrade(openSellTrade3);
        doubleClosedRecord.recordTrade(openSellTrade4);
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
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getLastOrder());
        assertEquals(Order.sellAt(10, Decimal.NaN, Decimal.NaN), doubleClosedRecord.getLastOrder());
        // Last BUY order
        assertNull(emptyRecord.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), openedRecord.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), closedRecord.getLastOrder(Order.OrderType.BUY));
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getLastOrder(Order.OrderType.BUY));
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
        assertEquals(Order.buyAt(7, Decimal.NaN, Decimal.NaN), doubleOpenedRecord.getLastEntry());
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