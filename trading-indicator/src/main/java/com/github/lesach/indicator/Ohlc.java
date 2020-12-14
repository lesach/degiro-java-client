package com.github.lesach.indicator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Ohlc
{
   private LocalDateTime Date;

   private BigDecimal Open;

   private BigDecimal High;

   private BigDecimal Low;

   private BigDecimal Close;

   private BigDecimal Volume;

   private BigDecimal AdjClose;
}
