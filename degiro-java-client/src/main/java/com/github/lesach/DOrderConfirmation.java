package com.github.lesach;

import java.math.BigDecimal;

/**
 *
 * @author indiketa
 */
public class DOrderConfirmation {

    private String confirmationId;
    private BigDecimal freeSpaceNew;
    private String message;
    private long status;
    private String statusText;

    public String getConfirmationId() {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId) {
        this.confirmationId = confirmationId;
    }

    public BigDecimal getFreeSpaceNew() {
        return freeSpaceNew;
    }

    public void setFreeSpaceNew(BigDecimal freeSpaceNew) {
        this.freeSpaceNew = freeSpaceNew;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
    
    

}
