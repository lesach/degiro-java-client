package com.github.lesach.controller;

import com.github.lesach.DPortfolioProducts;
import com.github.lesach.config.AppConfig;
import com.github.lesach.Context;
import com.github.lesach.tableview.PositionTableViewSchema;
import com.github.lesach.utils.Format;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.math.BigDecimal;
import java.util.Date;

public class PositionsScheduledService extends ScheduledService<ObservableList<PositionTableViewSchema>> {
    private final Context context;
    private final ObservableList<PositionTableViewSchema> positionsData;

    PositionsScheduledService(Context context,
                              ObservableList<PositionTableViewSchema> positionsData) {
        this.context = context;
        this.positionsData = positionsData;
    }

    /**
     * Load positions
     * @return positions
     */
    protected Task<ObservableList<PositionTableViewSchema>> createTask(){
        return new Task<ObservableList<PositionTableViewSchema>>(){
            @Override
            protected ObservableList<PositionTableViewSchema> call() {
                if (AppConfig.getTest()) {
                    FilteredList<PositionTableViewSchema> list = positionsData.filtered(t -> t.getId().equals("123456"));
                    PositionTableViewSchema s;
                    if (list.isEmpty()) {
                        s = new PositionTableViewSchema("123456",
                                "ProductSchema",
                                "Place",
                                BigDecimal.valueOf(987.4D),
                                546L,
                                "EUR",
                                        BigDecimal.valueOf(987564.54D),
                                                BigDecimal.valueOf(123.74D),
                                                        BigDecimal.valueOf(21.4D),
                                                                BigDecimal.valueOf(-65.45D),
                                Format.formatDate(new Date())
                        );
                        positionsData.add(s);
                    }
                    else {
                        s = list.get(0);
                        s.update("123456",
                                "ProductSchema",
                                "Place",
                                BigDecimal.valueOf(1987.4D),
                                546L,
                                "EUR",
                                        BigDecimal.valueOf(64.54D),
                                                BigDecimal.valueOf(123.74D),
                                                        BigDecimal.valueOf(21.4D),
                                                                BigDecimal.valueOf(-105.45D),
                                Format.formatDate(new Date())
                        );
                    }
                }
                else {
                    DPortfolioProducts products = context.getPortfolio();
                    products.getActive().forEach(p -> {
                        FilteredList<PositionTableViewSchema> list = positionsData.filtered(t -> t.getId() == p.getId());
                        PositionTableViewSchema s;
                        if (list.isEmpty()) {
                            s = new PositionTableViewSchema(p.getId(),
                                    p.getProduct(),
                                    p.getExchangeBriefCode(),
                                    p.getPrice(),
                                    p.getContractSize(),
                                    p.getCurrency(),
                                    p.getValue(),
                                    p.getTodayPlBase(),
                                    (p.getPrice().subtract(p.getClosePrice())).multiply(BigDecimal.valueOf(p.getContractSize())),
                                    p.getPlBase(),
                                    Format.formatDate(p.getLastUpdate())
                            );
                            positionsData.add(s);
                        } else {
                            s = list.get(0);
                            s.update(p.getId(),
                                    p.getProduct(),
                                    p.getExchangeBriefCode(),
                                    p.getPrice(),
                                    p.getContractSize(),
                                    p.getCurrency(),
                                    p.getValue(),
                                    p.getTodayPlBase(),
                                    (p.getPrice().subtract(p.getClosePrice())).multiply(BigDecimal.valueOf(p.getContractSize())),
                                    p.getPlBase(),
                                    Format.formatDate(p.getLastUpdate())
                            );
                        }
                    });
                    positionsData.removeIf(o -> products.getActive().stream().anyMatch(e -> e.getId().equals(o.getId())));
                }
                return positionsData;
            }
        };
    }
}
