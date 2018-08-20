package org.ta4j.core.buildon;

import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;

/**
 * Extended implementation of Strategy and BaseStrategy.
 * BuildOn rule means the rule when position (Trade) is open, the strategy checks the rule
 * whether it should build on the actual position.
 */
public class BaseStrategyBuildOn extends BaseStrategy implements StrategyBuildOn {

    /** The buildOn rule: used to build on the existing position */
    private Rule buildOnRule;

    /**
     * Constructor.
     * @param entryRule the entry rule
     * @param buildOnRule the buildOn rule
     * @param exitRule the exit rule
     */
    public BaseStrategyBuildOn(Rule entryRule, Rule buildOnRule, Rule exitRule) {
        this(null, entryRule, buildOnRule, exitRule, 0);
    }

    /**
     * Constructor.
     * @param entryRule the entry rule
     * @param buildOnRule the buildOn rule
     * @param exitRule the exit rule
     * @param unstablePeriod strategy will ignore possible signals at <code>index</code> < <code>unstablePeriod</code>
     */
    public BaseStrategyBuildOn(Rule entryRule, Rule buildOnRule, Rule exitRule, int unstablePeriod) {
        this(null, entryRule, buildOnRule, exitRule, unstablePeriod);
    }

    /**
     * Constructor.
     * @param name the name of the strategy
     * @param entryRule the entry rule
     * @param buildOnRule the buildOn rule
     * @param exitRule the exit rule
     */
    public BaseStrategyBuildOn(String name, Rule entryRule, Rule buildOnRule, Rule exitRule) {
        this(name, entryRule, buildOnRule, exitRule, 0);
    }

    /**
     * Constructor.
     * @param name the name of the strategy
     * @param entryRule the entry rule
     * @param buildOnRule the buildOn rule
     * @param exitRule the exit rule
     * @param unstablePeriod strategy will ignore possible signals at <code>index</code> < <code>unstablePeriod</code>
     */
    public BaseStrategyBuildOn(String name, Rule entryRule, Rule buildOnRule, Rule exitRule, int unstablePeriod) {
        super(name, entryRule, exitRule, unstablePeriod);
        this.buildOnRule = buildOnRule;
    }

    @Override
    public Rule getBuildOnRule() {
        return this.buildOnRule;
    }

    @Override
    public boolean shouldBuildOn(int index, TradingRecord tradingRecord) {
        boolean shouldBuildOn = StrategyBuildOn.super.shouldBuildOn(index, tradingRecord);
        traceShouldBuildOn(index, shouldBuildOn);
        return shouldBuildOn;
    }

    /**
     * Traces the shouldEnter() method calls.
     * @param index the bar index
     * @param buildOn true if the strategy should build on open position, false otherwise
     */
    protected void traceShouldBuildOn(int index, boolean buildOn) {
        log.trace(">>> {}#shouldEnter({}): {}", className, index, buildOn);
    }
}
