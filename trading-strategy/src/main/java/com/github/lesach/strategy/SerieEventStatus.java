
package com.github.lesach.strategy;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SerieEventStatus
{
    private LocalDateTime date;
    private boolean verified;
}
