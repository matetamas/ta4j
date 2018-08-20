package org.ta4j.core.analysis.criteria;

import org.junit.Test;
import org.ta4j.core.*;
import org.ta4j.core.mocks.MockTimeSeries;

import static org.junit.Assert.*;

public class TotalNetProfitCriterionTest {

    private Double commissionRate = 0.003;
    private Double highprecision = TATestsUtils.TA_OFFSET;

    @Test
    public void calculateOnlyWithGainTrades() {
        MockTimeSeries series = new MockTimeSeries(100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(
                Order.buyAt(0,series), Order.sellAt(2,series),
                Order.buyAt(3,series), Order.sellAt(5,series));

        AnalysisCriterion profit = new TotalNetProfitCriterion(commissionRate);
        assertEquals((1.1 - 2 * commissionRate) * (1.05 - 2 * commissionRate),
                profit.calculate(series, tradingRecord), highprecision);
    }

    @Test
    public void calculateOnlyWithLossTrades() {
        MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(
                Order.buyAt(0,series), Order.sellAt(1,series),
                Order.buyAt(2,series), Order.sellAt(5,series));

        AnalysisCriterion profit = new TotalNetProfitCriterion(commissionRate);
        assertEquals((0.95 - 2 * commissionRate) * (0.7 - 2 * commissionRate),
                profit.calculate(series, tradingRecord), highprecision);
    }

    @Test
    public void calculateProfitWithTradesThatStartSelling() {
        MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(
                Order.sellAt(0,series), Order.buyAt(1,series),
                Order.sellAt(2,series), Order.buyAt(5,series));

        AnalysisCriterion profit = new TotalNetProfitCriterion(commissionRate);
        assertEquals((1 / 0.95 - 2 * commissionRate) * (1 / 0.7 - 2 * commissionRate),
                profit.calculate(series, tradingRecord), highprecision);
    }

    @Test
    public void calculateWithNoTradesShouldReturn1() {
        MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);

        AnalysisCriterion profit = new TotalNetProfitCriterion(commissionRate);
        assertEquals(1d, profit.calculate(series, new BaseTradingRecord()), highprecision);
    }

    @Test
    public void calculateWithOpenedTradeShouldReturn1() {
        MockTimeSeries series = new MockTimeSeries(100, 95, 100, 80, 85, 70);
        AnalysisCriterion profit = new TotalNetProfitCriterion(commissionRate);
        Trade trade = new Trade();
        assertEquals(1d, profit.calculate(series, trade), highprecision);
        trade.operate(0);
        assertEquals(1.0 - commissionRate, profit.calculate(series, trade), highprecision);
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = new TotalNetProfitCriterion(commissionRate);
        assertTrue(criterion.betterThan(2.0, 1.5));
        assertFalse(criterion.betterThan(1.5, 2.0));
    }
}