package org.ta4j.core.analysis.criteria;

import org.ta4j.core.Decimal;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;

/**
 * Total profit criterion.
 *
 * Differs from the TotalProfitCriterion with the use of the entry and exit commissions.
 */
public class TotalNetProfitCriterion extends TotalProfitCriterion {

    private Double normalCommissionRate;

    public TotalNetProfitCriterion(Double normalCommissionRate) {
        this.normalCommissionRate = normalCommissionRate;
    }

    /**
     * Calculates the profit of a trade (Buy and sell).
     *
     * @param series a time series
     * @param trade a trade
     * @return the profit of the trade
     */
    protected double calculateProfit(TimeSeries series, Trade trade) {
        Decimal profit = Decimal.ONE;
        if (trade.isClosed()) {
            // use price of entry/exit order, if NaN use close price of underlying time series
            Decimal exitClosePrice = trade.getExit().getPrice().isNaN() ?
                    series.getBar(trade.getExit().getIndex()).getClosePrice() : trade.getExit().getPrice();
            Decimal entryClosePrice = trade.getEntry().getPrice().isNaN() ?
                    series.getBar(trade.getEntry().getIndex()).getClosePrice() : trade.getEntry().getPrice();

            if (trade.getEntry().isBuy()) {
                profit = exitClosePrice.dividedBy(entryClosePrice);
            } else {
                profit = entryClosePrice.dividedBy(exitClosePrice);
            }
            profit = profit.minus(Decimal.valueOf(2 * normalCommissionRate));
        } else if (trade.isOpened()) {
            profit = profit.minus(Decimal.valueOf(normalCommissionRate));
        }
        return profit.doubleValue();
    }
}
