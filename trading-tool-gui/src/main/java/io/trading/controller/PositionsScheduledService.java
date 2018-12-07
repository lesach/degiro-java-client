package io.trading.controller;

import cat.indiketa.degiro.model.DPortfolioProducts;
import io.trading.config.AppConfig;
import io.trading.model.Context;
import io.trading.model.tableview.PositionTableViewSchema;
import io.trading.utils.Format;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.Date;

public class PositionsScheduledService extends ScheduledService<ObservableList<PositionTableViewSchema>> {
    private final ObservableList<PositionTableViewSchema> positionsData = FXCollections.observableArrayList(PositionTableViewSchema.extractor());
    private final Context context;

    PositionsScheduledService(Context context) {
        this.context = context;
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
                                987.4D,
                                546L,
                                "EUR",
                                987564.54D,
                                123.74D,
                                21.4D,
                                -65.45D,
                                Format.formatDate(new Date())
                        );
                        positionsData.add(s);
                    }
                    else {
                        s = list.get(0);
                        s.update("123456",
                                "ProductSchema",
                                "Place",
                                1987.4D,
                                546L,
                                "EUR",
                                64.54D,
                                123.74D,
                                21.4D,
                                -105.45D,
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
                                    p.getPrice().doubleValue(),
                                    p.getContractSize(),
                                    p.getCurrency(),
                                    p.getValue().doubleValue(),
                                    p.getTodayPlBase().doubleValue(),
                                    ((p.getPrice().doubleValue() - p.getClosePrice().doubleValue()) * p.getContractSize()),
                                    p.getPlBase().doubleValue(),
                                    Format.formatDate(p.getLastUpdate())
                            );
                            positionsData.add(s);
                        } else {
                            s = list.get(0);
                            s.update(p.getId(),
                                    p.getProduct(),
                                    p.getExchangeBriefCode(),
                                    p.getPrice().doubleValue(),
                                    p.getContractSize(),
                                    p.getCurrency(),
                                    p.getValue().doubleValue(),
                                    p.getTodayPlBase().doubleValue(),
                                    ((p.getPrice().doubleValue() - p.getClosePrice().doubleValue()) * p.getContractSize()),
                                    p.getPlBase().doubleValue(),
                                    Format.formatDate(p.getLastUpdate())
                            );
                        }
                    });
                    positionsData.removeIf(o -> products.getActive().stream().anyMatch(e -> e.getId() == o.getId()));
                }
                return positionsData;
            }
        };
    }
}
