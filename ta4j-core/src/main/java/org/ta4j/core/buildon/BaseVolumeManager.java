package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;

public class BaseVolumeManager implements VolumeManager {
    private Decimal volume;

    public BaseVolumeManager(Decimal volume) {
        this.volume = volume;
    }

    @Override
    public Decimal calculateAmount(Order order) {
        return calculateAmount(order.getPrice());
    }

    @Override
    public Decimal calculateAmount(Decimal price) {
        return volume;
    }
}
