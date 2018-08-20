package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;
import org.ta4j.core.Order;

public interface VolumeManager {
    Decimal calculateAmount(Order order);

    Decimal calculateAmount(Decimal price);
}