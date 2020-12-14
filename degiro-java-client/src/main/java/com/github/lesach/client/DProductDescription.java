package com.github.lesach.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author indiketa
 */
@Getter
@Setter
public class DProductDescription implements Cloneable {

    private String feedQuality;
    private long orderBookDepth;
    private String vwdIdentifierType;
    private String vwdId;
    private boolean qualitySwitchable;
    private boolean qualitySwitchFree;
    private long vwdModuleId;
    private long id;
    private String name;
    private String isin;
    private BigDecimal contractSize;
    private String exchangeId;
    private String symbol;
    private String productType;
    private int productTypeId;
    private boolean tradable;
    private List<DOrderTime> orderTimeTypes = null;
    private boolean gtcAllowed;
    private List<DOrderType> buyOrderTypes = null;
    private List<DOrderType> sellOrderTypes = null;
    private boolean marketAllowed;
    private boolean limitHitOrderAllowed;
    private boolean stoplossAllowed;
    private boolean stopLimitOrderAllowed;
    private boolean joinOrderAllowed;
    private boolean trailingStopOrderAllowed;
    private boolean combinedOrderAllowed;
    private boolean sellAmountAllowed;
    private boolean isFund;
    private BigDecimal closePrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date closePriceDate;
    private String category;
    private String currency;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.isin);
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
        final DProductDescription other = (DProductDescription) obj;
        if (!Objects.equals(this.isin, other.isin)) {
            return false;
        }
        return true;
    }
    
    @Override
    public DProductDescription clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        return (DProductDescription) clone;
    }


}
