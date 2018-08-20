package org.ta4j.core.buildon;

import org.junit.Test;
import org.ta4j.core.Decimal;
import org.ta4j.core.Order;
import org.ta4j.core.Trade;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BaseTradingRecordBuildOnTest {
    @Test
    public void test() {
        Comparator<Trade> fifo = Comparator.comparingInt(o -> o.getEntry().getIndex());
        PriorityQueue<Trade> trades = new PriorityQueue<>(fifo);
        Trade trade1 = new Trade(
                Order.buyAt(7, Decimal.valueOf(2), Decimal.valueOf(3)),
                Order.sellAt(4, Decimal.valueOf(5), Decimal.valueOf(6))
        );
        Trade trade2 = new Trade(
                Order.buyAt(6, Decimal.valueOf(7), Decimal.valueOf(8)),
                Order.sellAt(9, Decimal.valueOf(10), Decimal.valueOf(12))
        );
        Trade trade3 = new Trade(
                Order.buyAt(13, Decimal.valueOf(14), Decimal.valueOf(15)),
                Order.sellAt(16, Decimal.valueOf(17), Decimal.valueOf(18))
        );
        trades.add(trade1);
        trades.add(trade2);
        trades.add(trade3);

        for (Trade trade : trades) {
            System.out.println(trade.toString());
        }
        System.out.println();

        Trade trade4 = new Trade(
                Order.buyAt(1, Decimal.valueOf(14), Decimal.valueOf(15)),
                Order.sellAt(16, Decimal.valueOf(17), Decimal.valueOf(18))
        );
        trades.add(trade4);
        for (Trade trade : trades) {
            System.out.println(trade.toString());
        }
        System.out.println();
        Trade trade5 = trades.peek();
        System.out.println(trade5);
    }

    @Test
    public void test2() {
        PriorityQueue<Trade> trades = new PriorityQueue<>();
        Trade trade5 = trades.peek();
        System.out.println(trade5);
    }
}