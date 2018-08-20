package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;

import java.math.BigDecimal;

public class Cash {
    private Decimal availableCash;
    private boolean withCommission;

    public Cash(Decimal availableCash, boolean withCommission) {
        this.availableCash = availableCash;
        this.withCommission = withCommission;
    }

    public boolean withdrawal(Decimal price, Decimal amount) {
        Decimal investment = price.multipliedBy(amount);
        Decimal commission = calculateCommission(investment);
        Decimal withdrawAmount = investment.plus(commission);
        if (withdrawAmount.isLessThan(availableCash)) {
            this.availableCash = this.availableCash.minus(withdrawAmount);
            return true;
        } else {
            return false;
        }
    }

    public boolean deposit(Decimal price, Decimal amount) {
        Decimal investment = price.multipliedBy(amount);
        Decimal commission = calculateCommission(investment);
        Decimal deposit = investment.minus(commission);
        this.availableCash = this.availableCash.plus(deposit);
        return true;
    }

    public Decimal getAvailableCash() {
        return availableCash;
    }

    private Decimal calculateCommission(Decimal investment) {
        double strictCommission = investment.doubleValue() * Constants.NORMAL_COMMISSION_RATE;
        if (withCommission && strictCommission > Constants.NORMAL_MIN_COMMISSION) {
            Decimal temp = Decimal.valueOf(Constants.NORMAL_COMMISSION_RATE.toString())
                    .multipliedBy(investment);
            BigDecimal res = temp.getDelegate().setScale(0, BigDecimal.ROUND_HALF_UP);
            return Decimal.valueOf(res);
        } else if (withCommission) {
            return Decimal.valueOf(Constants.NORMAL_MIN_COMMISSION);
        } else {
            return Decimal.ZERO;
        }
    }
}
