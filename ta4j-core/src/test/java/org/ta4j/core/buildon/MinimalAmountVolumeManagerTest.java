package org.ta4j.core.buildon;

import org.junit.Assert;
import org.junit.Test;
import org.ta4j.core.Decimal;
import org.ta4j.core.Order;

public class MinimalAmountVolumeManagerTest {
    private double delta = 0.1;

    @Test
    public void testDecimalCalculateAmount() {
        Decimal price = Decimal.valueOf(100);
        VolumeManager vm = new MinimalAmountVolumeManager(1000);
        Decimal res = vm.calculateAmount(price);
        Assert.assertEquals(10, res.doubleValue(), delta);

        Decimal price1 = Decimal.valueOf(101);
        VolumeManager vm1 = new MinimalAmountVolumeManager(1000);
        Decimal res1 = vm1.calculateAmount(price1);
        Assert.assertEquals(9, res1.doubleValue(), delta);

        Decimal price2 = Decimal.valueOf(99);
        VolumeManager vm2 = new MinimalAmountVolumeManager(1000);
        Decimal res2 = vm2.calculateAmount(price2);
        Assert.assertEquals(10, res2.doubleValue(), delta);
    }

    @Test
    public void testOrderCalculateAmount() {
        Decimal price = Decimal.valueOf(100);
        Order open = Order.buyAt(0, price, null);
        VolumeManager vm = new MinimalAmountVolumeManager(1000);
        Decimal res = vm.calculateAmount(open);
        Assert.assertEquals(10, res.doubleValue(), delta);

        Decimal price1 = Decimal.valueOf(101);
        Order open1 = Order.buyAt(0, price1, null);
        VolumeManager vm1 = new MinimalAmountVolumeManager(1000);
        Decimal res1 = vm1.calculateAmount(open1);
        Assert.assertEquals(9, res1.doubleValue(), delta);

        Decimal price2 = Decimal.valueOf(99);
        Order open2 = Order.buyAt(0, price2, null);
        VolumeManager vm2 = new MinimalAmountVolumeManager(1000);
        Decimal res2 = vm2.calculateAmount(open2);
        Assert.assertEquals(10, res2.doubleValue(), delta);
    }
}