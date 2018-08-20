package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;

public class VolumeManagerImpl implements VolumeManager {
    private Decimal volume;

    public VolumeManagerImpl(Decimal volume) {
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
