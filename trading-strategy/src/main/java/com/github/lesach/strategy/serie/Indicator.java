package com.github.lesach.strategy.serie;

import com.github.lesach.strategy.EIndicatorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Indicator implements Cloneable {
    private EIndicatorType indicatorType;
    private List<IndicatorParameter> parameters = new ArrayList<IndicatorParameter>();

    public Indicator(EIndicatorType indicatorType)
    {
        this.indicatorType = indicatorType;
    }

    public Indicator(EIndicatorType indicatorType, Map<String, BigDecimal> parameters)
    {
        this.indicatorType = indicatorType;
        for(Map.Entry<String, BigDecimal> p : parameters.entrySet())
            this.parameters.add(new IndicatorParameter() {{ setName(p.getKey()); setValue(p.getValue()); }});
    }

    @Override
    public Indicator clone() throws CloneNotSupportedException {
        Indicator result = (Indicator) super.clone();
        result.indicatorType = this.indicatorType;
        for (IndicatorParameter p : this.parameters)
            result.parameters.add(new IndicatorParameter() {{ setName(p.getName()); setValue(p.getValue()); }});
        return result;
    }

    @Override
    public String toString() {
        String result = indicatorType.toString();
        if (this.parameters.size() == 0)
            return result;
        else
        {
            result += "(";
            result += Strings.join(this.parameters, ',');
            return result + ")";
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Indicator))
            return false;
        Indicator indicator = (Indicator) obj;
        if (indicator.indicatorType == indicatorType)
        {
            return this.parameters.stream().allMatch(p -> indicator.parameters.stream().anyMatch(i -> (i.getName().equals(p.getName())) && (i.getValue().equals(p.getValue()))));
        }
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return indicatorType.toString().hashCode() + parameters.stream().mapToInt(p -> p.getValue().hashCode()).sum();
    }


    public static List<Indicator> Indicators = Arrays.asList(
            new Indicator(EIndicatorType.Last),
            new Indicator(EIndicatorType.Bid),
            new Indicator(EIndicatorType.Ask),
            new Indicator(EIndicatorType.ADL),
            new Indicator(EIndicatorType.ADX),
            new Indicator(EIndicatorType.Aroon),
            new Indicator(EIndicatorType.ATR),
            new Indicator(EIndicatorType.BollingerBand),
            new Indicator(EIndicatorType.CCI),
            new Indicator(EIndicatorType.CMF),
            new Indicator(EIndicatorType.CMO),
            new Indicator(EIndicatorType.DEMA),
            new Indicator(EIndicatorType.DPO),
            new Indicator(EIndicatorType.EMA) {{
                setParameters(Collections.singletonList(new IndicatorParameter() {{
                    setName("Period");
                    setValue(BigDecimal.valueOf(10));
                }}));
            }},
            new Indicator(EIndicatorType.Envelope),
            new Indicator(EIndicatorType.Ichimoku),
            new Indicator(EIndicatorType.MACD),
            new Indicator(EIndicatorType.Momentum),
            new Indicator(EIndicatorType.OBV),
            new Indicator(EIndicatorType.PVT),
            new Indicator(EIndicatorType.ROC),
            new Indicator(EIndicatorType.RSI),
            new Indicator(EIndicatorType.SAR),
            new Indicator(EIndicatorType.SMA),
            new Indicator(EIndicatorType.TRIX),
            new Indicator(EIndicatorType.Volume),
            new Indicator(EIndicatorType.VROC),
            new Indicator(EIndicatorType.WMA),
            new Indicator(EIndicatorType.WPR),
            new Indicator(EIndicatorType.ZLEMA)
    );
}
