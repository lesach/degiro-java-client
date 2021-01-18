
package com.github.lesach.strategy;

import com.github.lesach.indicator.Ohlc;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Period
{
    private Ohlc ohlc;
    private LocalDateTime end;
}
