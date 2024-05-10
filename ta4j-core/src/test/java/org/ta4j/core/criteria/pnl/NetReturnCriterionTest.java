/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.criteria.pnl;

import org.junit.Ignore;
import org.junit.Test;
import org.ta4j.core.*;
import org.ta4j.core.analysis.cost.CostModel;
import org.ta4j.core.analysis.cost.FixedTransactionCostModel;
import org.ta4j.core.analysis.cost.LinearTransactionCostModel;
import org.ta4j.core.analysis.cost.ZeroCostModel;
import org.ta4j.core.criteria.AbstractCriterionTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

import java.util.function.Function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

public class NetReturnCriterionTest extends AbstractCriterionTest {

    public NetReturnCriterionTest(Function<Number, Num> numFunction) {
        super(params -> params.length == 1 ? new NetReturnCriterion((boolean) params[0]) : new NetReturnCriterion(),
                numFunction);
    }

    @Test
    public void calculateWithWinningLongPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 150, 200, 150, 100, 150);
//        FixedTransactionCostModel transactionCost = new FixedTransactionCostModel(1);
        CostModel transactionCost = new LinearTransactionCostModel(0.01);
        ZeroCostModel holdingCost = new ZeroCostModel();
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY, transactionCost, holdingCost);

        // entry price = 100 (cost = 1) => netPrice = 101, grossPrice = 100
        tradingRecord.enter(0, series.getBar(0).getClosePrice(), numOf(1));
        // exit price = 105 (cost = 1) => netPrice = 104, grossPrice = 105
        tradingRecord.exit(1, series.getBar(1).getClosePrice(),
                tradingRecord.getCurrentPosition().getEntry().getAmount());

        // entry price = 100 (cost = 1) => netPrice = 101, grossPrice = 100
        tradingRecord.enter(2, series.getBar(2).getClosePrice(), numOf(1));
        // exit price = 120 (cost = 1) => netPrice = 119, grossPrice = 120
        tradingRecord.exit(5, series.getBar(5).getClosePrice(),
                tradingRecord.getCurrentPosition().getEntry().getAmount());

        // include base percentage
        AnalysisCriterion retWithBase = getCriterion();
        double expected = ((150 * 0.99) / (100 * 1.01)) * ((150 * 0.99) / (200 * 1.01));
        assertNumEquals(expected, retWithBase.calculate(series, tradingRecord));

        // exclude base percentage
        AnalysisCriterion retWithoutBase = getCriterion(false);
        assertNumEquals(expected - 1, retWithoutBase.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithWinningShortPosition() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 150, 200, 150, 100, 150);
//        FixedTransactionCostModel transactionCost = new FixedTransactionCostModel(1);
        CostModel transactionCost = new LinearTransactionCostModel(0.01);
        ZeroCostModel holdingCost = new ZeroCostModel();
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.SELL, transactionCost, holdingCost);


        // entry price = 100 (cost = 1) => netPrice = 101, grossPrice = 100
        tradingRecord.enter(2, series.getBar(2).getClosePrice(), numOf(1));
        // exit price = 120 (cost = 1) => netPrice = 119, grossPrice = 120
        tradingRecord.exit(4, series.getBar(4).getClosePrice(),
                tradingRecord.getCurrentPosition().getEntry().getAmount());

        // include base percentage
        AnalysisCriterion retWithBase = getCriterion();
        double expected = ((200 * 0.99) / (100 * 1.01));
        assertNumEquals(expected, retWithBase.calculate(series, tradingRecord));

        // exclude base percentage
        AnalysisCriterion retWithoutBase = getCriterion(false);
        assertNumEquals(expected - 1, retWithoutBase.calculate(series, tradingRecord));
    }
    @Test
    public void calculateWithLosingLongPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        // include base percentage
        AnalysisCriterion retWithBase = getCriterion();
        assertNumEquals(0.95 * 0.7, retWithBase.calculate(series, tradingRecord));

        // exclude base percentage
        AnalysisCriterion retWithoutBase = getCriterion(false);
        assertNumEquals(0.95 * 0.7 - 1, retWithoutBase.calculate(series, tradingRecord));
    }

    @Test
    public void calculateReturnWithWinningShortPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(5, series));

        // include base percentage
        AnalysisCriterion retWithBase = getCriterion();
        assertNumEquals(1.05 * 1.30, retWithBase.calculate(series, tradingRecord));

        // exclude base percentage
        AnalysisCriterion retWithoutBase = getCriterion(false);
        assertNumEquals(1.05 * 1.30 - 1, retWithoutBase.calculate(series, tradingRecord));
    }

    @Test
    public void calculateReturnWithLosingShortPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 100, 80, 85, 130);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(5, series));

        // include base percentage
        AnalysisCriterion retWithBase = getCriterion();
        assertNumEquals(0.95 * 0.70, retWithBase.calculate(series, tradingRecord));

        // exclude base percentage
        AnalysisCriterion retWithoutBase = getCriterion(false);
        assertNumEquals(0.95 * 0.70 - 1, retWithoutBase.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithNoPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);

        // with base percentage should return 1
        AnalysisCriterion retWithBase = getCriterion();
        assertNumEquals(1d, retWithBase.calculate(series, new BaseTradingRecord()));

        // without base percentage should return 0
        AnalysisCriterion retWithoutBase = getCriterion(false);
        assertNumEquals(0, retWithoutBase.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithOpenedPosition() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);

        // with base percentage should return 1
        AnalysisCriterion retWithBase = getCriterion();
        Position position1 = new Position();
        assertNumEquals(1d, retWithBase.calculate(series, position1));
        position1.operate(0);
        assertNumEquals(1d, retWithBase.calculate(series, position1));

        // without base percentage should return 0
        AnalysisCriterion retWithoutBase = getCriterion(false);
        Position position2 = new Position();
        assertNumEquals(0, retWithoutBase.calculate(series, position2));
        position2.operate(0);
        assertNumEquals(0, retWithoutBase.calculate(series, position2));
    }

    @Test
    public void testCalculateOneOpenPosition() {
        // with base percentage should return 1
        openedPositionUtils.testCalculateOneOpenPositionShouldReturnExpectedValue(numFunction, getCriterion(), 1);

        // without base percentage should return 0
        openedPositionUtils.testCalculateOneOpenPositionShouldReturnExpectedValue(numFunction, getCriterion(false), 0);
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(2.0), numOf(1.5)));
        assertFalse(criterion.betterThan(numOf(1.5), numOf(2.0)));
    }
}
