package com.github.lesach.webapp.model;

import com.github.lesach.client.DPortfolioProduct;
import com.github.lesach.client.DPrice;
import com.github.lesach.client.DProductDescription;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Date;

@Getter
@Setter
public class ProductSchema {
    private String productId;
    private String productName;
    private String currency;
    // Direct
    private BigDecimal ask;
    private BigDecimal bid;
    private Long priceTime;
    // Last
    private BigDecimal last;
    private Long lastTime;
    private String vwdId;
    private BigDecimal closePrice;
    private Long closePriceDate;
    private String productTypeId;
    private String symbol;
    private String isin;
    private Boolean tradable;


    /**
     * Basic Constructor
     */
    public ProductSchema() {
        this("-1",
                "",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0L,
                BigDecimal.ZERO,
                0L,
                "",
                BigDecimal.ZERO,
                0L,
                "",
                "",
                "",
                "",
                false);
    }

    /**
     * Full constructor
     * @param id
     * @param name
     * @param bid
     * @param ask
     * @param priceTime
     * @param last*
     * @param lastTime
     * @param vwdId
     * @param closePrice
     * @param closePriceDate
     * @param productTypeId
     * @param currency
     * @param symbol
     * @param isin
     * @param tradable
     */
    public ProductSchema(String id,
                         String name,
                         BigDecimal bid,
                         BigDecimal ask,
                         Long priceTime,
                         BigDecimal last,
                         Long lastTime,
                         String vwdId,
                         BigDecimal closePrice,
                         Long closePriceDate,
                         String productTypeId,
                         String currency,
                         String symbol,
                         String isin,
                         Boolean tradable) {
        this.productId = id;
        this.productName = name;
        this.bid =  bid;
        this.ask =  ask;
        this.priceTime = priceTime;
        this.last =  last;
        this.lastTime = lastTime;
        this.vwdId = vwdId;
        this.closePrice =  closePrice;
        this.closePriceDate = closePriceDate;
        this.productTypeId = productTypeId;
        this.currency = currency;
        this.symbol = symbol;
        this.isin = isin;
        this.tradable = tradable;
    }

    /**
     * Update prices
     * @param ask
     * @param bid
     */
    public void update(BigDecimal ask,
                       BigDecimal bid) {
        this.setAsk(ask);
        this.setBid(bid);
    }

    /**
     * Update product prices
     * @param prod new price
     */
    public void adopt(DPrice prod) {
        if (prod != null) {
            if (prod.getBid() != null) {
                setBid(prod.getBid());
            }
            if (prod.getAsk() != null) {
                setAsk(prod.getAsk());
            }
            if (prod.getLast() != null) {
                setLast(prod.getLast());
            }
            if (prod.getLastTime() != null) {
                setLastTime(prod.getLastTime().toEpochSecond(ZoneOffset.UTC));
            }
            setPriceTime(new Date().getTime());
        }
    }


    /**
     *
     * @param prod to adopt
     */
    public void adopt(DProductDescription prod) {
        if (prod != null) {
            setProductId(Long.toString(prod.getId()));
            setProductName(prod.getName());
            setIsin(prod.getIsin());
            setCurrency(prod.getCurrency());
            setSymbol(prod.getSymbol());
            setTradable(prod.isTradable());
            setVwdId(prod.getVwdId());
            setProductTypeId(prod.getProductTypeId().toString());
            setClosePrice(prod.getClosePrice());
            setTradable(prod.isTradable());
            if (prod.getClosePriceDate() != null)
                setClosePriceDate(prod.getClosePriceDate().getTime());
            else
                setClosePriceDate(0l);
        }
    }

    /**
     *
     * @param prod to adopt
     */
    public void adopt(DPortfolioProduct prod){
        if (prod == null) {
            return;
        }
        setProductId(prod.getId());
        if (Strings.isNullOrEmpty(getProductName())) {
            setProductName(prod.getProduct());
        }
        setCurrency(prod.getCurrency());
        if (prod.getPrice() != null) {
            setAsk(prod.getPrice());
            setBid(prod.getPrice());
        }

        if (prod.getLastUpdate() != null) {
            setLastTime(prod.getLastUpdate().getTime());
        }
    }


    /**
     *
     * @param basic to adopt
     */
    public void adopt(BasicSchema basic){
        if (basic == null)
            return;
        setProductId(basic.getProductId());
        if (Strings.isNullOrEmpty(getProductName())) {
            setProductName(basic.getProductName());
        }
        setCurrency(basic.getCurrency());
    }
}
