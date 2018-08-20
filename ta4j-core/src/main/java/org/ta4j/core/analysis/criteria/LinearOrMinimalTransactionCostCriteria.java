package org.ta4j.core.analysis.criteria;

import org.ta4j.core.Order;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;

import java.util.function.Function;

/*
*   A linear or minimal transaction cost criteria, automatically chooses
*   higher value of the two.
*
*   - InitialAmount not necessary here, that's why it's left private in
*   LinearTransactionCostCriterion
*   - Two private methods (getOrderCost and getTradeCost) left private,
*   because signature contains InitialAmount which is not necessary,
*   instead implemented two methods with same name, but signature without
*   InitialAmount
*   - Thus constructors are written without using InitialAmount
* */
public class LinearOrMinimalTransactionCostCriteria extends LinearTransactionCostCriterion {

    private Function<Order, Double> commissionFunction = order ->
            Math.max(a * order.getPrice().multipliedBy(order.getAmount()).doubleValue(), b);

    public LinearOrMinimalTransactionCostCriteria(double a) {
        super(0, a);
    }

    public LinearOrMinimalTransactionCostCriteria(double a, double b) {
        super(0, a, b);
    }

    @Override
    public double calculate(TimeSeries series, TradingRecord tradingRecord) {
        double totalCosts = 0d;

        for (Trade trade : tradingRecord.getTrades()) {
            double tradeCost = getTradeCost(trade);
            totalCosts += tradeCost;
        }

        // Special case: if the current trade is open
        Trade currentTrade = tradingRecord.getCurrentTrade();
        if (currentTrade.isOpened()) {
            totalCosts += getOrderCost(currentTrade.getEntry());
        }

        return totalCosts;
    }

    /**
     * @param order a trade order
     * @return the absolute order cost
     */
    protected double getOrderCost(Order order) {
        double orderCost = 0d;
        if (order != null) {
            return commissionFunction.apply(order);
        }
        return orderCost;
    }

    /**
     * @param trade a trade
     * @return the absolute total cost of all orders in the trade
     */
    protected double getTradeCost(Trade trade) {
        double totalTradeCost = 0d;
        if (trade != null) {
            if (trade.getEntry() != null) {
                totalTradeCost = getOrderCost(trade.getEntry());
                if (trade.getExit() != null) {
                    totalTradeCost += getOrderCost(trade.getExit());
                }
            }
        }
        return totalTradeCost;
    }
}
