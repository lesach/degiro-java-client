
package com.github.lesach.serie;

import java.math.BigDecimal;

public class IndicatorParameter
{
    public String Name;
    public BigDecimal Value;

    @Override
    public String toString()
    {
        return this.Name + "=" + this.Value.toString();
    }
}
