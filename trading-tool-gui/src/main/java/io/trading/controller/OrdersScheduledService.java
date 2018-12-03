package io.trading.controller;

import cat.indiketa.degiro.model.DOrder;
import io.trading.config.AppConfig;
import io.trading.model.Context;
import io.trading.model.tableview.OrderTableViewSchema;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.List;

public class OrdersScheduledService extends ScheduledService<ObservableList<OrderTableViewSchema>> {
    private final ObservableList<OrderTableViewSchema> ordersData = FXCollections.observableArrayList(OrderTableViewSchema.extractor());
    private final Context context;

    public OrdersScheduledService(Context context) {
        this.context = context;
    }

    public ObservableList<OrderTableViewSchema> getOrdersData() {
        return ordersData;
    }

    /**
     * Load positions
     * @return
     */
    protected Task<ObservableList<OrderTableViewSchema>> createTask(){
        return new Task<ObservableList<OrderTableViewSchema>>(){
            @Override
            protected ObservableList<OrderTableViewSchema> call() {
                if (AppConfig.getTest()) {
                    FilteredList<OrderTableViewSchema> list = ordersData.filtered(t -> t.getId() == "123456Id");
                    OrderTableViewSchema s;
                    if (list.isEmpty()) {
                        s = new OrderTableViewSchema("123456Id",
                                "S",
                                "ProductSchema",
                                "LIMIT",
                                15.65d,
                                "EUR",
                                98l
                        );
                        ordersData.add(s);
                    }
                    else {
                        s = list.get(0);
                        s.update("123456Id",
                                "B",
                                "ProductSchema",
                                "LIMIT",
                                12.45d,
                                "EUR",
                                100l
                        );
                    }
                }
                else {
                    List<DOrder> orders = context.getOrders();
                    orders.forEach(o -> {
                        FilteredList<OrderTableViewSchema> list = ordersData.filtered(t -> t.getId().equals(o.getId()));
                        OrderTableViewSchema s;
                        if (list.isEmpty()) {
                            s = new OrderTableViewSchema(o.getId(),
                                    o.getBuysell().getStrValue(),
                                    o.getProduct(),
                                    o.getOrderType().getStrValue(),
                                    o.getPrice().doubleValue(),
                                    o.getCurrency(),
                                    o.getQuantity()
                            );
                            ordersData.add(s);
                        }
                        else {
                            s = list.get(0);
                            s.update(o.getId(),
                                    o.getBuysell().getStrValue(),
                                    o.getProduct(),
                                    o.getOrderType().getStrValue(),
                                    o.getPrice().doubleValue(),
                                    o.getCurrency(),
                                    o.getQuantity()
                            );
                        }
                    });
                }
                return ordersData;
            }
        };
    }
}
