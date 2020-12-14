
package com.github.lesach.strategy.serie;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class IndicatorParameter
{
    private String name;
    private BigDecimal value;

    @Override
    public String toString()
    {
        return this.name + "=" + this.value.toString();
    }
}
