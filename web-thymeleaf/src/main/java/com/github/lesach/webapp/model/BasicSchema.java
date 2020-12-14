package com.github.lesach.webapp.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BasicSchema extends ProductSchema {

    private Long quantity;
    private BigDecimal total;
    private Boolean error;

    /**
     * Constructor
     */
    public BasicSchema() {
        this.quantity = 0L;
        this.total =  BigDecimal.ZERO;
        this.error = false;
    }
}
