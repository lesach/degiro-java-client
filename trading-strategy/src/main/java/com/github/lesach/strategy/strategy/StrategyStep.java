
package com.github.lesach.strategy.strategy;

import com.github.lesach.strategy.EPeriodInstantType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StrategyStep
{
    private EPeriodInstantType periodInstantType;
    private List<StrategyStepConditionGroup> groups = new ArrayList<>();

    @Override
    public String toString() {
        return this.getPeriodInstantType().toString();
    }
}
