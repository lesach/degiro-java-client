
package com.github.lesach.strategy;

import java.time.LocalDateTime;

public class SerieEventStatus
{
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime _date) {
        this.date = _date;
    }

    private LocalDateTime date;
    public boolean Verified;
}
