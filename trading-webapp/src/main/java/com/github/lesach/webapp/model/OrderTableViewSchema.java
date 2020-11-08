package com.github.lesach.webapp.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderTableViewSchema extends BasicSchema {
    private String id;
    private String buyOrSell;
    private String orderType;
    private BigDecimal limit;
    private String DUMMY;

    /**
     *
     * @param id
     * @param buyOrSell
     * @param product
     * @param orderType
     * @param limit
     * @param currency
     * @param quantity
     */
    public OrderTableViewSchema(String id,
                                String buyOrSell,
                                String product,
                                String orderType,
                                BigDecimal limit,
                                String currency,
                                long quantity) {
        super();
        this.setProductName(product);
        this.setCurrency(currency);
        this.setQuantity(quantity);
        this.id = id;
        this.buyOrSell = buyOrSell;
        this.orderType = orderType;
        this.limit = limit;
        this.DUMMY = null;
    }

    /**
     *
     * @param id
     * @param buyOrSell
     * @param product
     * @param orderType
     * @param limit
     * @param currency
     * @param quantity
     */
    public void update (String id,
                        String buyOrSell,
                        String product,
                        String orderType,
                        BigDecimal limit,
                        String currency,
                        long quantity) {
        this.setId(id);
        this.setBuyOrSell(buyOrSell);
        this.setProductName(product);
        this.setOrderType(orderType);
        this.setLimit(limit);
        this.setCurrency(currency);
        this.setQuantity(quantity);
    }
}
