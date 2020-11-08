package com.github.lesach.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author indiketa
 */
public class DPrice {

    private String issueId;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal last;
    private LocalDateTime lastTime;
    private String vwdProductName;
    private LocalDateTime refreshTime;

    public LocalDateTime getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(LocalDateTime refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }

    public String getVwdProductName() {
        return vwdProductName;
    }

    public void setVwdProductName(String vwdProductName) {
        this.vwdProductName = vwdProductName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.bid);
        hash = 59 * hash + Objects.hashCode(this.ask);
        hash = 59 * hash + Objects.hashCode(this.last);
        hash = 59 * hash + Objects.hashCode(this.lastTime);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DPrice other = (DPrice) obj;
        if (!Objects.equals(this.bid, other.bid)) {
            return false;
        }
        if (!Objects.equals(this.ask, other.ask)) {
            return false;
        }
        if (!Objects.equals(this.last, other.last)) {
            return false;
        }
        if (!Objects.equals(this.lastTime, other.lastTime)) {
            return false;
        }
        return true;
    }

}
