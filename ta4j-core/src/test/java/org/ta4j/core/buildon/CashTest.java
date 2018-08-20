package org.ta4j.core.buildon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Decimal;

public class CashTest {
    private Decimal price;
    private Decimal amount;
    private double delta = 0.1;

    @Before
    public void setUp() {
        price = Decimal.valueOf(1000);
        amount = Decimal.valueOf(100);
    }

    @Test
    public void testWithDrawalFail() {
        Decimal availableCash = Decimal.valueOf(1);
        Cash cash = new Cash(availableCash, false);
        boolean res = cash.withdrawal(price, amount);
        Assert.assertFalse(res);
    }

    @Test
    public void testWithDrawalSuccess() {
        Decimal availableCash = Decimal.valueOf(1000000);
        Cash cash = new Cash(availableCash, false);
        boolean res = cash.withdrawal(price, amount);
        Assert.assertTrue(res);
        Assert.assertEquals(900000, cash.getAvailableCash().doubleValue(), delta);
    }

    @Test
    public void testDeposit() {
        Decimal availableCash = Decimal.valueOf(1000000);
        Cash cash = new Cash(availableCash, false);
        boolean res = cash.deposit(price, amount);
        Assert.assertTrue(res);
        Assert.assertEquals(1100000, cash.getAvailableCash().doubleValue(), delta);
    }

    @Test
    public void testMinimalCostandPercentalCostWithDrawal() {
        Decimal minimalCostAvailableCash = Decimal.valueOf(1000000);
        Cash cash = new Cash(minimalCostAvailableCash, true); // 1 000 000
        boolean res = cash.withdrawal(price, amount); // - 100 000 - 300
        Assert.assertTrue(res);
        Assert.assertEquals(899700, cash.getAvailableCash().doubleValue(), delta);

        Decimal percentalCostAvailableCash = Decimal.valueOf(1000000);
        price = Decimal.valueOf(1000);
        amount = Decimal.valueOf(66);
        cash = new Cash(percentalCostAvailableCash, true);  // 1 000 000
        res = cash.withdrawal(price, amount); // - 66 000 - 199
        Assert.assertTrue(res);
        Assert.assertEquals(933801, cash.getAvailableCash().doubleValue(), delta);
    }

    @Test
    public void testMinimalCostandPercentalCostDeposit() {
        Decimal minimalCostAvailableCash = Decimal.valueOf(1000000); // 1 000 000
        Cash cash = new Cash(minimalCostAvailableCash, true);
        boolean res = cash.deposit(price, amount); // + 100 000 - 300
        Assert.assertTrue(res);
        Assert.assertEquals(1099700, cash.getAvailableCash().doubleValue(), delta);

        Decimal percentalCostAvailableCash = Decimal.valueOf(1000000);
        price = Decimal.valueOf(1000);
        amount = Decimal.valueOf(66);
        cash = new Cash(percentalCostAvailableCash, true); // 1 000 000
        res = cash.deposit(price, amount); // + 66 000 - 199
        Assert.assertTrue(res);
        Assert.assertEquals(1065801, cash.getAvailableCash().doubleValue(), delta);
    }

    @Test
    public void testExtremeCommissionRoundingWithDrawal() {
        Decimal minimalCostAvailableCash = Decimal.valueOf(1000000);
        price = Decimal.valueOf(100);
        amount = Decimal.valueOf(666);
        Cash cash = new Cash(minimalCostAvailableCash, true); // 1 000 000
        boolean res = cash.withdrawal(price, amount); // - 66 600 - 199,8 (will be: - 200)
        Assert.assertTrue(res);
        Assert.assertEquals(933200, cash.getAvailableCash().doubleValue(), delta);

    }
}