package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;

public class MinimalAmountVolumeManager implements VolumeManager {
    private Decimal minimalAmount;

    public MinimalAmountVolumeManager(int minimalAmount) {
        this.minimalAmount = Decimal.valueOf(minimalAmount);
    }

    @Override
    public Decimal calculateAmount(Order order) {
        return calculateAmount(order.getPrice());
    }

    @Override
    public Decimal calculateAmount(Decimal price) {
        return (minimalAmount.dividedBy(price)).floor();
    }
}
