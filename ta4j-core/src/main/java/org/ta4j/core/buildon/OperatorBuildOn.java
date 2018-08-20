package org.ta4j.core.buildon;

import org.ta4j.core.Decimal;

public interface OperatorBuildOn {
    void operate(int index, Decimal price, Decimal amount);
}