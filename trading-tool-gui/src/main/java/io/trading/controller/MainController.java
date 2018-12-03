package io.trading.controller;

import cat.indiketa.degiro.model.*;
import com.google.gson.GsonBuilder;
import io.trading.config.AppConfig;
import io.trading.display.LongDateStringConverter;
import io.trading.model.Context;
import io.trading.model.InputOrder;
import io.trading.model.tableview.ProductSchema;
import io.trading.model.tableview.OrderTableViewSchema;
import io.trading.model.tableview.PositionTableViewSchema;
import io.trading.provider.SubscriptionProvider;
import io.trading.utils.Format;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private final Context context = new Context();
    private final SubscriptionProvider subscriptionProvider = new SubscriptionProvider(context);

    // Call product tab
    private ProductSchema callProductSchema;
    private ProductSchema getCallProductSchema() {
        return callProductSchema;
    }
    private void setCallProductSchema(ProductSchema callProductSchema) {
        if (this.callProductSchema != null) {
            bindCallProduct(false);
        }
        this.callProductSchema = callProductSchema;
        if (this.callProductSchema != null) {
            bindCallProduct(true);
        }
        subscriptionProvider.manageSubscription();
    }
    private InputOrder callOrder = new InputOrder();


    // Table viex
    private ObservableList<PositionTableViewSchema> positionsData;
    PositionsScheduledService positionsScheduledService;
    private ObservableList<OrderTableViewSchema> ordersData;
    OrdersScheduledService ordersScheduledService;


    // Credentials pane
    @FXML private TextField txtUser;
    @FXML private TextField txtPassword;
    @FXML private Button btnConnect;
    @FXML private Circle shpConnected;

    // Client pane
    @FXML private TextField txtName;
    @FXML private TextField txtTotal;
    @FXML private TextField txtShares;
    @FXML private TextField txtCash;
    @FXML private TextField txtAvailable;
    @FXML private TextField txtVariation;

    // Turbo Call pane
    @FXML private TextField txtCallProductSearch;
    @FXML private Button btnCallProductSearch;
    @FXML private Label lblCallProductName;
    @FXML private Label lblCallProductIsin;
    @FXML private Label lblCallProductAsk;
    @FXML private Label lblCallProductBid;
    @FXML private Label lblCallProductTime;
    @FXML private Label lblCallProductLastValue;
    @FXML private Label lblCallProductLastTime;
    @FXML private Button btnCallProductBuy;
    @FXML private Label lblCallProductBuyQuantity;
    @FXML private Label lblCallProductBuyTotal;
    @FXML private TextField txtCallProductBuyAmount;

    // Position Table
    @FXML TableView<PositionTableViewSchema> tabPositions;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionProduct;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionPlace;
    @FXML TableColumn<PositionTableViewSchema, Double> colPositionQuantity;
    @FXML TableColumn<PositionTableViewSchema, Double> colPositionPrice;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionCurrency;
    @FXML TableColumn<PositionTableViewSchema, Double> colPositionTotal;
    @FXML TableColumn<PositionTableViewSchema, Double> colPositionDailyPL;
    @FXML TableColumn<PositionTableViewSchema, Double> colPositionDailyVariation;
    @FXML TableColumn<PositionTableViewSchema, Double> colPositionTotalPL;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionTime;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionSell;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionStopLoss;

    // Order table
    @FXML TableView<OrderTableViewSchema> tabOrders;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderBuyOrSell;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderProduct;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderType;
    @FXML TableColumn<OrderTableViewSchema, Double> colOrderLimit;
    @FXML TableColumn<OrderTableViewSchema, Double> colOrderQuantity;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderCurrency;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderDelete;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Controller loading...");
        // Positions table
        // Orders table
        colPositionProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colPositionPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        colPositionQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPositionPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPositionCurrency.setCellValueFactory(new PropertyValueFactory<>("currency"));
        colPositionTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPositionDailyPL.setCellValueFactory(new PropertyValueFactory<>("dailyPL"));
        colPositionDailyVariation.setCellValueFactory(new PropertyValueFactory<>("dailyVariation"));
        colPositionTotalPL.setCellValueFactory(new PropertyValueFactory<>("totalPL"));
        colPositionTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPositionSell.setCellValueFactory(new PropertyValueFactory<>("DummySell"));
        Callback<TableColumn<PositionTableViewSchema, String>,
                TableCell<PositionTableViewSchema, String>> positionSellCellFactory
                = new Callback<TableColumn<PositionTableViewSchema, String>,
                        TableCell<PositionTableViewSchema, String>>() {

                    @Override
                    public TableCell<PositionTableViewSchema, String> call(final TableColumn<PositionTableViewSchema, String> param) {
                        return new TableCell<PositionTableViewSchema, String>() {

                            final Button btn = new Button("X");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        PositionTableViewSchema p = getTableView().getItems().get(getIndex());
                                        logger.info("Sell product: " + p.getProductName() + " -> " + Format.formatBigDecimal(p.getPrice()));
                                        // Generate a new order. Signature:
                                        DNewOrder order = new DNewOrder(DOrderAction.SELL,
                                                DOrderType.LIMITED,
                                                DOrderTime.DAY,
                                                p.getId(),
                                                Double.valueOf(p.getQuantity()).longValue(),
                                                new BigDecimal(p.getBid()),
                                                null);
                                        if (sendOrder(order))
                                            p.setError(false);
                                        else
                                            p.setError(true);
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                    }
                };
        colPositionSell.setCellFactory(positionSellCellFactory);


        colPositionSell.setCellValueFactory(new PropertyValueFactory<>("DummyStopLoss"));
        Callback<TableColumn<PositionTableViewSchema, String>,
                TableCell<PositionTableViewSchema, String>> positionStopLossCellFactory
                = new Callback<TableColumn<PositionTableViewSchema, String>,
                TableCell<PositionTableViewSchema, String>>() {

            @Override
            public TableCell<PositionTableViewSchema, String> call(final TableColumn<PositionTableViewSchema, String> param) {
                return new TableCell<PositionTableViewSchema, String>() {

                    final Button btn = new Button("X");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                PositionTableViewSchema p = getTableView().getItems().get(getIndex());
                                logger.info("Stop loss on product: " + p.getProductName() + " -> " + Format.formatBigDecimal(p.getPrice()));
                                // Generate a new order. Signature:
                                DNewOrder order = new DNewOrder(DOrderAction.SELL,
                                        DOrderType.LIMITED_STOP_LOSS,
                                        DOrderTime.DAY,
                                        p.getId(),
                                        Double.valueOf(p.getQuantity()).longValue(),
                                        new BigDecimal(p.getBid()),
                                        null);
                                if (sendOrder(order))
                                    p.setError(false);
                                else
                                    p.setError(true);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };
        colPositionStopLoss.setCellFactory(positionStopLossCellFactory);

        colPositionProduct.setCellFactory(column -> new TableCell<PositionTableViewSchema, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                }
                else {
                    setText(item);
                    PositionTableViewSchema p = getTableView().getItems().get(getIndex());
                    // Style all dates in March with a different color.
                    if (p.isError()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });


        positionsScheduledService = new PositionsScheduledService(context);
        positionsScheduledService.setPeriod(Duration.seconds(30));
        positionsScheduledService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                logger.info("Positions refreshed: " + t.getSource().getValue());
                positionsData = (ObservableList<PositionTableViewSchema>) t.getSource().getValue();
                tabPositions.setItems(positionsData);
                subscriptionProvider.mergeDescriptionProducts(positionsData);
            }
        });

        // Orders table
        colOrderBuyOrSell.setCellValueFactory(new PropertyValueFactory<>("buyOrSell"));
        colOrderProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colOrderType.setCellValueFactory(new PropertyValueFactory<>("orderType"));
        colOrderLimit.setCellValueFactory(new PropertyValueFactory<>("limit"));
        colOrderQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colOrderCurrency.setCellValueFactory(new PropertyValueFactory<>("currency"));
        colOrderDelete.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        Callback<TableColumn<OrderTableViewSchema, String>,
                TableCell<OrderTableViewSchema, String>> orderCellFactory
                = new Callback<TableColumn<OrderTableViewSchema, String>,
                TableCell<OrderTableViewSchema, String>>() {
            @Override
            public TableCell<OrderTableViewSchema, String> call(final TableColumn<OrderTableViewSchema, String> param) {
                return new TableCell<OrderTableViewSchema, String>() {
                    final Button btn = new Button("X");
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                OrderTableViewSchema o = getTableView().getItems().get(getIndex());
                                logger.info("Delete order: " + o.getProductName() + " -> " + Format.formatBigDecimal(o.getLimit()));

                                DPlacedOrder deleted = context.deleteOrder(o.getId()); // orderId obtained in getOrders()
                                if (deleted.getStatus() != 0) {
                                    String tmp = o.getProductName();
                                    o.setProductName("");
                                    o.setError(true);
                                    o.setProductName(tmp);
                                }
                                else
                                    o.setError(false);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };
        colOrderDelete.setCellFactory(orderCellFactory);
        colOrderProduct.setCellFactory(column -> new TableCell<OrderTableViewSchema, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    OrderTableViewSchema o = getTableView().getItems().get(getIndex());
                    // Style all dates in March with a different color.
                    if (o.isError()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });


        ordersScheduledService = new OrdersScheduledService(context);
        ordersScheduledService.setPeriod(Duration.seconds(30));
        ordersScheduledService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                logger.info("orders refreshed: " + t.getSource().getValue());
                ordersData = (ObservableList<OrderTableViewSchema>) t.getSource().getValue();
                tabOrders.setItems(ordersData);
                subscriptionProvider.mergeDescriptionProducts(ordersData);
            }
        });

        
        tabOrders.setItems(ordersData);
        logger.info("Controller is now loaded");
    }


    /**
     * Check if user and password are not empty
     * @return true if OK
     */
    private boolean checkCredentials() {
        return checkEmptyTextField(txtUser) & checkEmptyTextField(txtPassword);
    }

    /**
     * Check if a text field is empty
     * @return true if OK
     */
    private boolean checkEmptyTextField(TextField field) {
        boolean result = true;
        // User name
        String s = field.getText();
        if (s.isEmpty()) {
            field.getStyleClass().add("error");
            result = false;
        }
        else
            field.getStyleClass().remove("error");
        return result;
    }


    /**
     * Fill Client panel
     */
    private void displayPortFolio() {
        DPortfolioSummary ps = context.getPortfolioSummary();
       if (ps == null) {
           txtName.clear();
           txtTotal.clear();
           txtShares.clear();
           txtCash.clear();
           txtAvailable.clear();
           txtVariation.clear();
       }
       else {
           txtName.setText(this.context.getUsername());
           txtTotal.setText(Format.formatBigDecimal(ps.getTotal()));
           txtShares.setText(Format.formatBigDecimal(ps.getPortVal()));
           txtCash.setText(Format.formatBigDecimal(ps.getCash()));
           txtAvailable.setText(Format.formatBigDecimal(ps.getFreeSpace()));
           txtVariation.setText(Format.formatBigDecimal(ps.getPl()));
       }
    }


    /**
     * Update displayed prices
     * @param price to update
     */
    private void updatePrices(DPrice price) {
        if (positionsData != null) {
            FilteredList<PositionTableViewSchema> positionList = positionsData.filtered(t -> Long.toString(t.getId()).equals(price.getIssueId()));
            if (!positionList.isEmpty()) {
                positionList.get(0).update(price.getAsk(), price.getBid());
            }
        }
        if (ordersData != null) {
            FilteredList<OrderTableViewSchema> orderList = ordersData.filtered(t -> t.getId().equals(price.getIssueId()));
            if (!orderList.isEmpty()) {
                orderList.get(0).update(price.getAsk(), price.getBid());
            }
        }
    }

    /**
     * Connect to API
     * @param event trigger
     */
    @FXML protected void handleConnectButtonAction(ActionEvent event) {
        logger.info("Connect button pressed");
        if (checkCredentials()) {
            context.setUsername(txtUser.getText());
            context.setPassword(txtPassword.getText());
            if (context.Connect()) {
                txtUser.getStyleClass().remove("error");
                txtPassword.getStyleClass().remove("error");
                // Register a price update listener (called on price update)
                context.setPriceListener(new DPriceListener() {
                    @Override
                    public void priceChanged(DPrice price) {
                        logger.info("Price changed: " + new GsonBuilder().setPrettyPrinting().create().toJson(price));
                        Optional<ProductSchema> product = subscriptionProvider.getProducts().values().stream().filter(p -> p.getVwdId().equals(price.getIssueId())).findFirst();
                        Platform.runLater(() -> {
                            product.ifPresent(p -> {
                                p.adopt(price);
                                if (callOrder.getProductId() == p.getProductId())
                                    callOrder.setPrice(price.getAsk());

                            });
                            updatePrices(price);

                            //computeCallQuantityAndTotal();
                        });
                    }
                });
                positionsScheduledService.start();
                shpConnected.setFill(Paint.valueOf("#55FF55"));
            }
            else {
                txtUser.getStyleClass().add("error");
                txtPassword.getStyleClass().add("error");
                shpConnected.setFill(Paint.valueOf("#FF5555"));
            }
            displayPortFolio();
        }
        else {
            shpConnected.setFill(Paint.valueOf("#FF5555"));
        }
    }


    /**
     * Check if product name is not empty
     * @return true if OK
     */
    private boolean checkCallProductSearch() {
        return checkEmptyTextField(txtCallProductSearch);
    }

    /**
     * Fill Call panel
     */
    private void bindCallProduct(boolean bind) {
        if (bind) {
            // ProductSchema
            lblCallProductName.textProperty().bindBidirectional(callProductSchema.productNameProperty());
            lblCallProductIsin.textProperty().bindBidirectional(callProductSchema.isinProperty());
            lblCallProductAsk.textProperty().bindBidirectional(callProductSchema.askProperty(), new NumberStringConverter());
            lblCallProductBid.textProperty().bindBidirectional(callProductSchema.bidProperty(), new NumberStringConverter());
            lblCallProductLastValue.textProperty().bindBidirectional(callProductSchema.lastProperty(), new NumberStringConverter());
            lblCallProductLastTime.textProperty().bindBidirectional(callProductSchema.priceTimeProperty(), new LongDateStringConverter());
            btnCallProductBuy.disableProperty().bind(callProductSchema.tradableProperty().not());
            txtCallProductBuyAmount.disableProperty().bind(callProductSchema.tradableProperty().not());
            lblCallProductTime.textProperty().bindBidirectional(callProductSchema.priceTimeProperty(), new LongDateStringConverter());
            // Order
            txtCallProductBuyAmount.textProperty().bindBidirectional(callOrder.amountProperty(), new NumberStringConverter());
            callOrder.priceProperty().bind(callProductSchema.askProperty());
            lblCallProductBuyQuantity.textProperty().bindBidirectional(callOrder.quantityProperty(), new NumberStringConverter());
            lblCallProductBuyTotal.textProperty().bindBidirectional(callOrder.totalProperty(), new NumberStringConverter());
        }
        else {
            // ProductSchema
            btnCallProductBuy.disableProperty().unbind();
            txtCallProductBuyAmount.disableProperty().unbind();
            lblCallProductName.textProperty().unbindBidirectional(callProductSchema.productNameProperty());
            lblCallProductIsin.textProperty().unbindBidirectional(callProductSchema.isinProperty());
            lblCallProductAsk.textProperty().unbindBidirectional(callProductSchema.askProperty());
            lblCallProductBid.textProperty().unbindBidirectional(callProductSchema.bidProperty());
            lblCallProductLastValue.textProperty().unbindBidirectional(callProductSchema.lastProperty());
            lblCallProductLastTime.textProperty().unbindBidirectional(callProductSchema.priceTimeProperty());
            lblCallProductTime.textProperty().unbindBidirectional(callProductSchema.priceTimeProperty());
            lblCallProductBuyQuantity.setText("0");
            txtCallProductBuyAmount.setDisable(true);
            btnCallProductBuy.setDisable(true);
            // Order
            callOrder.priceProperty().unbind();
            Platform.runLater(() -> callOrder.setPrice(0d));
            txtCallProductBuyAmount.textProperty().unbindBidirectional(callOrder.amountProperty());
            lblCallProductBuyQuantity.textProperty().unbindBidirectional(callOrder.quantityProperty());
            lblCallProductBuyTotal.textProperty().unbindBidirectional(callOrder.totalProperty());
        }
    }


    /**
     * Search Call product
     * @param event trigger
     */
    @FXML protected void handleCallProductSearchButtonAction(ActionEvent event) {
        logger.info("Call ProductSchema Search button pressed");
        setCallProductSchema(null);
        if (checkCallProductSearch()) {
            List<DProductDescription> descriptions = this.context.searchProducts(txtCallProductSearch.getText());
            if (descriptions != null && descriptions.size() == 1) {
                subscriptionProvider.mergeDescriptionProducts(descriptions);
                setCallProductSchema(subscriptionProvider.getProducts().get(descriptions.get(0).getId()));
            }
        }
    }

    /**
     * Create an order to Buy Call ProductSchema
     * @param event trigger
     */
    @FXML protected void handleCallProductBuyButtonAction(ActionEvent event) {
        logger.info("Call ProductSchema Buy button pressed");
        FilteredList<PositionTableViewSchema> list = this.positionsData.filtered(t -> t.getId() == 123456L);
        if (this.getCallProductSchema() != null) {
            if (this.getCallProductSchema().isTradable())
            {
                logger.info("Creating Buy order");
                // Generate a new order. Signature:
                // public DNewOrder(DOrderAction action, DOrderType orderType, DOrderTime timeType, long productId, long size, BigDecimal limitPrice, BigDecimal stopPrice)
                if (AppConfig.getTest()) {
                    OrderTableViewSchema order = new OrderTableViewSchema(
                            "Order1",
                            DOrderAction.SELL.toString(),
                            getCallProductSchema().getProductName(),
                            DOrderType.LIMITED.toString(),
                            Format.parseBigDecimal(lblCallProductAsk.getText()).doubleValue(),
                            "EUR",
                            Long.parseLong(lblCallProductBuyQuantity.getText())
                            );
                    ordersData.add(order);
                }
                else {
                    DNewOrder order = new DNewOrder(DOrderAction.SELL,
                            DOrderType.LIMITED,
                            DOrderTime.DAY,
                            getCallProductSchema().getProductId(),
                            Long.parseLong(lblCallProductBuyQuantity.getText()),
                            Format.parseBigDecimal(lblCallProductAsk.getText()),
                            null);
                    sendOrder(order);
                }
            }
            else{
                logger.info("The call product has to be Tradable");
            }
        }
        else {
            logger.info("A call product has to be selected");
        }
    }


    /**
     * Send order
     * @param order to send
     */
    private boolean sendOrder(DNewOrder order) {
        try {
            DOrderConfirmation confirmation = context.checkOrder(order);

            if (!confirmation.getConfirmationId().isEmpty()) {
                DPlacedOrder placed = context.confirmOrder(order, confirmation);
                if (placed.getStatus() != 0) {
                    throw new RuntimeException("Order not placed: " + placed.getStatusText());
                }
                return true;
            }
        }
        catch (Exception e) {
            logger.error("ERROR in sendOrder", e);
        }
        return false;
    }


    /**
     * Compute Call order
     */
    private void computeCallQuantityAndTotal() {
        BigDecimal amount = Format.parseBigDecimal(txtCallProductBuyAmount.getText());
        BigDecimal price = Format.parseBigDecimal(lblCallProductAsk.getText());
        if (amount == null || price == null) {
            lblCallProductBuyQuantity.setText("0");
            lblCallProductBuyTotal.setText("0");
        }
        else {
            BigDecimal quantity = amount.divide(price, RoundingMode.FLOOR);
            lblCallProductBuyQuantity.setText(Long.toString(quantity.longValueExact()));
            lblCallProductBuyTotal.setText(Format.formatBigDecimal(quantity.multiply(price)));
        }
    }

    /**
     * Compute total amount and quantity
     * @param event trigger
     */
    @FXML protected void handletxtCallProductBuyAmountAction(ActionEvent event) {
        logger.info("Refresh price while modifying amount");

    }

}