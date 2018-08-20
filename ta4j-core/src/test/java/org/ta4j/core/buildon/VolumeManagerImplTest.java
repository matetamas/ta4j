package org.ta4j.core.buildon;

import org.junit.Test;
import org.ta4j.core.Decimal;
import org.ta4j.core.Order;

import static org.junit.Assert.assertEquals;

public class VolumeManagerImplTest {
    private double delta = 0.1;

    @Test
    public void testDecimalCalculateAmount() {
        VolumeManager vm = new VolumeManagerImpl(Decimal.valueOf(10));
        Decimal res = vm.calculateAmount(Decimal.NaN);
        assertEquals(10, res.doubleValue(), delta);
    }

    @Test
    public void testOrderCalculateAmount() {
        Order open = Order.buyAt(0, null, null);
        VolumeManager vm = new VolumeManagerImpl(Decimal.valueOf(10));
        Decimal res = vm.calculateAmount(open);
        assertEquals(10, res.doubleValue(), delta);
    }
}