package io.trading.controller;

import cat.indiketa.degiro.model.*;
import com.google.gson.GsonBuilder;
import io.trading.config.AppConfig;
import io.trading.display.FlashingTableCell;
import io.trading.display.LongDateStringConverter;
import io.trading.model.Context;
import io.trading.model.InputOrder;
import io.trading.model.Sanity;
import io.trading.model.tableview.BasicSchema;
import io.trading.model.tableview.ProductSchema;
import io.trading.model.tableview.OrderTableViewSchema;
import io.trading.model.tableview.PositionTableViewSchema;
import io.trading.provider.SubscriptionProvider;
import io.trading.utils.Format;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
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


    // Put product tab
    private ProductSchema putProductSchema;
    private ProductSchema getPutProductSchema() {
        return putProductSchema;
    }
    private void setPutProductSchema(ProductSchema putProductSchema) {
        if (this.putProductSchema != null) {
            bindPutProduct(false);
        }
        this.putProductSchema = putProductSchema;
        if (this.putProductSchema != null) {
            bindPutProduct(true);
        }
        subscriptionProvider.manageSubscription();
    }
    private InputOrder putOrder = new InputOrder();

    // Table viex
    private final ObservableList<PositionTableViewSchema> positionsData = FXCollections.observableArrayList(PositionTableViewSchema.extractor());
    private PositionsScheduledService positionsScheduledService;

    private final ObservableList<OrderTableViewSchema> ordersData = FXCollections.observableArrayList(OrderTableViewSchema.extractor());
    private OrdersScheduledService ordersScheduledService;

    private final Sanity sanity = new Sanity();
    private SanityScheduledService sanityScheduledService;


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
    private static final Duration HIGHLIGHT_TIME = Duration.millis(500);
    @FXML private TextField txtCallProductSearch;
    @FXML private Button btnCallProductSearch;
    @FXML private Label lblCallProductName;
    @FXML private Label lblCallProductIsin;
    @FXML private Rectangle lblCallProductAskRectangle;
    @FXML private Label lblCallProductAsk;
    @FXML private Label lblCallProductBid;
    @FXML private Rectangle lblCallProductBidRectangle;
    @FXML private Label lblCallProductTime;
    @FXML private Rectangle lblCallProductPriceTimeRectangle;
    @FXML private Label lblCallProductLastValue;
    @FXML private Label lblCallProductLastTime;
    @FXML private Button btnCallProductBuy;
    @FXML private Label lblCallProductBuyQuantity;
    @FXML private Label lblCallProductBuyTotal;
    @FXML private TextField txtCallProductBuyAmount;
    // Put
    @FXML private TextField txtPutProductSearch;
    @FXML private Button btnPutProductSearch;
    @FXML private Label lblPutProductName;
    @FXML private Label lblPutProductIsin;
    @FXML private Rectangle lblPutProductAskRectangle;
    @FXML private Label lblPutProductAsk;
    @FXML private Label lblPutProductBid;
    @FXML private Rectangle lblPutProductBidRectangle;
    @FXML private Label lblPutProductTime;
    @FXML private Rectangle lblPutProductPriceTimeRectangle;
    @FXML private Label lblPutProductLastValue;
    @FXML private Label lblPutProductLastTime;
    @FXML private Button btnPutProductBuy;
    @FXML private Label lblPutProductBuyQuantity;
    @FXML private Label lblPutProductBuyTotal;
    @FXML private TextField txtPutProductBuyAmount;
    @FXML private Circle shpProducts;

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
    @FXML TableColumn<PositionTableViewSchema, String> colPositionAsk;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionBid;
    @FXML TableColumn<PositionTableViewSchema, String> colPositionPriceTime;
    @FXML private Circle shpPositions;

    // Order table
    @FXML TableView<OrderTableViewSchema> tabOrders;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderBuyOrSell;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderProduct;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderType;
    @FXML TableColumn<OrderTableViewSchema, Double> colOrderLimit;
    @FXML TableColumn<OrderTableViewSchema, Double> colOrderQuantity;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderCurrency;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderDelete;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderAsk;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderBid;
    @FXML TableColumn<OrderTableViewSchema, String> colOrderPriceTime;
    @FXML private Circle shpOrders;


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
                                                Long.parseLong(p.getId()),
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

        colPositionStopLoss.setCellValueFactory(new PropertyValueFactory<>("DummyStopLoss"));
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
                                        Long.parseLong(p.getId()),
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

        setPositionTableCellFlashing(colPositionAsk, "ask");
        setPositionTableCellFlashing(colPositionBid, "bid");
        setPositionTableCellFlashing(colPositionPriceTime, "priceTime");

        positionsScheduledService = new PositionsScheduledService(context, positionsData);
        positionsScheduledService.setPeriod(Duration.seconds(30));
        positionsScheduledService.setOnSucceeded((WorkerStateEvent t) -> {
            logger.info("Positions refreshed: " + t.getSource().getValue());
            //positionsData = (ObservableList<PositionTableViewSchema>) t.getSource().getValue();
            //tabPositions.setItems(positionsData);
            subscriptionProvider.mergeDescriptionProducts(positionsData);
        });
        tabPositions.setItems(positionsData);
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
        colOrderAsk.setCellValueFactory(new PropertyValueFactory<>("ask"));
        colOrderAsk.setCellFactory(c -> new FlashingTableCell<>(null, Pos.CENTER_RIGHT));

        colOrderBid.setCellValueFactory(new PropertyValueFactory<>("bid"));
        colOrderBid.setCellFactory(c -> new FlashingTableCell<>(null, Pos.CENTER_RIGHT));

        colOrderPriceTime.setCellValueFactory(new PropertyValueFactory<>("priceTime"));
        colOrderPriceTime.setCellFactory(c -> new FlashingTableCell<>(null, Pos.CENTER_RIGHT));

        tabOrders.setItems(ordersData);

        // Order refresh task
        ordersScheduledService = new OrdersScheduledService(context, ordersData);
        ordersScheduledService.setPeriod(Duration.seconds(30));
        ordersScheduledService.setOnSucceeded((WorkerStateEvent t) -> {
            logger.info("Orders refreshed: " + t.getSource().getValue());
            //ordersData = (ObservableList<OrderTableViewSchema>) t.getSource().getValue();
            //tabOrders.setItems(ordersData);
            subscriptionProvider.mergeDescriptionProducts(ordersData);
        });


        // Check
        sanityScheduledService = new SanityScheduledService(context,
                ordersData,
                positionsData,
                subscriptionProvider.getProducts(),
                subscriptionProvider.getSubscribedProducts(),
                callProductSchema,
                putProductSchema,
                sanity);
        sanityScheduledService.setPeriod(Duration.seconds(30));
        sanityScheduledService.setOnSucceeded((WorkerStateEvent t) -> logger.info("Sanity check refreshed"));

        // Label animations on price refresh
        setLabelFlashingAnimation(lblCallProductAskRectangle, lblCallProductAsk);
        setLabelFlashingAnimation(lblCallProductBidRectangle, lblCallProductBid);
        setLabelFlashingAnimation(lblCallProductPriceTimeRectangle, lblCallProductTime);
        setLabelFlashingAnimation(lblPutProductAskRectangle, lblPutProductAsk);
        setLabelFlashingAnimation(lblPutProductBidRectangle, lblPutProductBid);
        setLabelFlashingAnimation(lblPutProductPriceTimeRectangle, lblPutProductTime);

        // Initialize credentials
        txtUser.setText(AppConfig.getDegiroUserName());
        txtPassword.setText(AppConfig.getDegiroPassword());

        // Color binding: Connected
        ObjectBinding<Paint> connectedBinding = Bindings.createObjectBinding(() -> booleanToPaint(sanity.connectedProperty().get()), sanity.connectedProperty());
        connectedBinding.addListener((observable, oldValue, newValue) -> logger.info("Connected binding changed from " + oldValue + " to " + newValue));
        shpConnected.fillProperty().bind(connectedBinding);

        // Color binding: Positions
        ObjectBinding<Paint> positionsPriceRefreshPropertyBinding = Bindings.createObjectBinding(() -> booleanToPaint(sanity.positionsPriceRefreshProperty().get()), sanity.positionsPriceRefreshProperty());
        positionsPriceRefreshPropertyBinding.addListener((observable, oldValue, newValue) -> logger.info("Positions Price Refresh binding changed from " + oldValue + " to " + newValue));
        shpPositions.fillProperty().bind(positionsPriceRefreshPropertyBinding);

        // Color binding: Orders
        ObjectBinding<Paint> ordersPriceRefreshPropertyBinding = Bindings.createObjectBinding(() -> booleanToPaint(sanity.ordersPriceRefreshProperty().get()), sanity.ordersPriceRefreshProperty());
        ordersPriceRefreshPropertyBinding.addListener((observable, oldValue, newValue) -> logger.info("Orders Price Refresh binding changed from " + oldValue + " to " + newValue));
        shpOrders.fillProperty().bind(ordersPriceRefreshPropertyBinding);

        // Color binding: Products
        ObjectBinding<Paint> productsPriceRefreshPropertyBinding = Bindings.createObjectBinding(() -> booleanToPaint(sanity.productsPriceRefreshProperty().get()), sanity.productsPriceRefreshProperty());
        productsPriceRefreshPropertyBinding.addListener((observable, oldValue, newValue) -> logger.info("Products Price Refresh binding changed from " + oldValue + " to " + newValue));
        shpProducts.fillProperty().bind(productsPriceRefreshPropertyBinding);

        // Search button enable
        btnCallProductSearch.disableProperty().bind(Bindings.isEmpty(txtCallProductSearch.textProperty()));
        btnPutProductSearch.disableProperty().bind(Bindings.isEmpty(txtPutProductSearch.textProperty()));
        btnConnect.disableProperty().bind(Bindings.isEmpty(txtUser.textProperty()).or(Bindings.isEmpty(txtPassword.textProperty())));
        sanityScheduledService.start();

        logger.info("Controller is now loaded");
    }

    /**
     * Destroy context
     */
    public void close() {
        logger.info("Controller is closing...");
        if (positionsScheduledService.isRunning())
            positionsScheduledService.cancel();
        if (ordersScheduledService.isRunning())
            ordersScheduledService.cancel();
        if (sanityScheduledService.isRunning())
            sanityScheduledService.cancel();
    }

    /**
     * booleanToPaint
     * @param value To convert
     * @return Paint
     */
    private Paint booleanToPaint(boolean value) {
        if (value)
            return Paint.valueOf("#55FF55");
        else
            return Paint.valueOf("#FF5555");
    }

    /**
     * flashLabel
     * @param transition Animation to play
     */
    private void flashLabel(FadeTransition transition) {
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.playFromStart();
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
        for(ObservableList<? extends BasicSchema> l : Arrays.asList(positionsData, ordersData))
            l.filtered(t -> t.getVwdId().equals(price.getIssueId())).forEach(p ->p.update(price.getAsk(), price.getBid()));
    }

    /**
     * Connect to API
     * @param event trigger
     */
    @FXML protected void handleConnectButtonAction(ActionEvent event) {
        logger.info("Connect button pressed (" + event.getEventType().getName() + ")");
        if (checkCredentials()) {
            context.setUsername(txtUser.getText());
            context.setPassword(txtPassword.getText());
            if (context.Connect()) {
                txtUser.getStyleClass().remove("error");
                txtPassword.getStyleClass().remove("error");
                // Register a price update listener (called on price update)
                context.setPriceListener((DPrice price) -> {
                    logger.info("Price changed: " + new GsonBuilder().setPrettyPrinting().create().toJson(price));
                    Optional<ProductSchema> product = subscriptionProvider.getProducts().values().stream().filter(p -> p.getVwdId().equals(price.getIssueId())).findFirst();
                    Platform.runLater(() -> {
                        product.ifPresent(p -> {
                            p.adopt(price);
                            //if (callOrder.getProductId() == p.getProductId())
                                //callOrder.setPrice(price.getAsk());
                        });
                        updatePrices(price);
                    });
                });
                positionsScheduledService.start();
                ordersScheduledService.start();
                //shpConnected.setFill(Paint.valueOf("#55FF55"));
            }
            else {
                txtUser.getStyleClass().add("error");
                txtPassword.getStyleClass().add("error");
                //shpConnected.setFill(Paint.valueOf("#FF5555"));
            }
            displayPortFolio();
        }
        //else {
            //shpConnected.setFill(Paint.valueOf("#FF5555"));
        //}
    }


    /**
     * Check if product name is not empty
     * @return true if OK
     */
    private boolean checkCallProductSearch() {
        return checkEmptyTextField(txtCallProductSearch);
    }


    /**
     * Check if product name is not empty
     * @return true if OK
     */
    private boolean checkPutProductSearch() {
        return checkEmptyTextField(txtPutProductSearch);
    }

    /**
     * Fill Call panel
     */
    private void bindCallProduct(boolean bind) {
        bindProduct(bind,
            lblCallProductName,
            lblCallProductIsin,
            lblCallProductAsk,
            lblCallProductBid,
            lblCallProductLastValue,
            lblCallProductLastTime,
            btnCallProductBuy,
            txtCallProductBuyAmount,
            lblCallProductTime,
            lblCallProductBuyQuantity,
            lblCallProductBuyTotal,
            callOrder,
            callProductSchema);
    }


    /**
     * Fill Put panel
     */
    private void bindPutProduct(boolean bind) {
        bindProduct(bind,
                lblPutProductName,
                lblPutProductIsin,
                lblPutProductAsk,
                lblPutProductBid,
                lblPutProductLastValue,
                lblPutProductLastTime,
                btnPutProductBuy,
                txtPutProductBuyAmount,
                lblPutProductTime,
                lblPutProductBuyQuantity,
                lblPutProductBuyTotal,
                putOrder,
                putProductSchema);
    }

    /**
     * Search Call product
     * @param event trigger
     */
    @FXML protected void handleCallProductSearchButtonAction(ActionEvent event) {
        logger.info("Call ProductSchema Search button pressed (" + event.getEventType().getName() + ")");
        setCallProductSchema(null);
        callOrder.setProductId(0L);
        if (checkCallProductSearch()) {
            List<DProductDescription> descriptions = this.context.searchProducts(txtCallProductSearch.getText());
            if (descriptions != null && descriptions.size() == 1) {
                subscriptionProvider.mergeDescriptionProducts(descriptions);
                setCallProductSchema(subscriptionProvider.getProducts().get(Long.toString(descriptions.get(0).getId())));
                callOrder.setProductId(descriptions.get(0).getId());
            }
        }
    }

    /**
     * Create an order to Buy Call ProductSchema
     * @param event trigger
     */
    @FXML protected void handleCallProductBuyButtonAction(ActionEvent event) {
        logger.info("Call ProductSchema Buy button pressed (" + event.getEventType().getName() + ")");
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
                            Long.parseLong(getCallProductSchema().getProductId()),
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
     * Search Put product
     * @param event trigger
     */
    @FXML protected void handlePutProductSearchButtonAction(ActionEvent event) {
        logger.info("Put ProductSchema Search button pressed (" + event.getEventType().getName() + ")");
        setPutProductSchema(null);
        putOrder.setProductId(0L);
        if (checkPutProductSearch()) {
            List<DProductDescription> descriptions = this.context.searchProducts(txtPutProductSearch.getText());
            if (descriptions != null && descriptions.size() == 1) {
                subscriptionProvider.mergeDescriptionProducts(descriptions);
                setPutProductSchema(subscriptionProvider.getProducts().get(Long.toString(descriptions.get(0).getId())));
                putOrder.setProductId(descriptions.get(0).getId());
            }
        }
    }

    /**
     * Create an order to Buy Put ProductSchema
     * @param event trigger
     */
    @FXML protected void handlePutProductBuyButtonAction(ActionEvent event) {
        logger.info("Put ProductSchema Buy button pressed (" + event.getEventType().getName() + ")");
        if (this.getPutProductSchema() != null) {
            if (this.getPutProductSchema().isTradable())
            {
                logger.info("Creating Buy order");
                // Generate a new order. Signature:
                // public DNewOrder(DOrderAction action, DOrderType orderType, DOrderTime timeType, long productId, long size, BigDecimal limitPrice, BigDecimal stopPrice)
                if (AppConfig.getTest()) {
                    OrderTableViewSchema order = new OrderTableViewSchema(
                            "Order1",
                            DOrderAction.SELL.toString(),
                            getPutProductSchema().getProductName(),
                            DOrderType.LIMITED.toString(),
                            Format.parseBigDecimal(lblPutProductAsk.getText()).doubleValue(),
                            "EUR",
                            Long.parseLong(lblPutProductBuyQuantity.getText())
                    );
                    ordersData.add(order);
                }
                else {
                    DNewOrder order = new DNewOrder(DOrderAction.SELL,
                            DOrderType.LIMITED,
                            DOrderTime.DAY,
                            Long.parseLong(getPutProductSchema().getProductId()),
                            Long.parseLong(lblPutProductBuyQuantity.getText()),
                            Format.parseBigDecimal(lblPutProductAsk.getText()),
                            null);
                    sendOrder(order);
                }
            }
            else{
                logger.info("The put product has to be Tradable");
            }
        }
        else {
            logger.info("A put product has to be selected");
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
     * Set flashing cell
     * @param column to set
     * @param propertyName to bind
     */
    private void setPositionTableCellFlashing(TableColumn<PositionTableViewSchema, String> column, String propertyName) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(c -> new FlashingTableCell<>(null, Pos.CENTER_RIGHT));
    }

    /**
     * Set Label flashing animation
     * @param rectangle to flash
     * @param label attached
     */
    private void setLabelFlashingAnimation(Rectangle rectangle, Label label) {
        FadeTransition transition = new FadeTransition(HIGHLIGHT_TIME, rectangle);
        label.textProperty().addListener((observable) -> flashLabel(transition));
    }

    /**
     * Fill Put panel
     */
    private void bindProduct(boolean bind,
                             Label lblProductName,
                             Label lblProductIsin,
                             Label lblProductAsk,
                             Label lblProductBid,
                             Label lblProductLastValue,
                             Label lblProductLastTime,
                             Button btnProductBuy,
                             TextField txtProductBuyAmount,
                             Label lblProductTime,
                             Label lblProductBuyQuantity,
                             Label lblProductBuyTotal,
                             InputOrder order,
                             ProductSchema productSchema) {
        if (bind) {
            // ProductSchema
            lblProductName.textProperty().bindBidirectional(productSchema.productNameProperty());
            lblProductIsin.textProperty().bindBidirectional(productSchema.isinProperty());
            lblProductAsk.textProperty().bindBidirectional(productSchema.askProperty(), new NumberStringConverter());
            lblProductBid.textProperty().bindBidirectional(productSchema.bidProperty(), new NumberStringConverter());
            lblProductLastValue.textProperty().bindBidirectional(productSchema.lastProperty(), new NumberStringConverter());
            lblProductLastTime.textProperty().bindBidirectional(productSchema.lastTimeProperty(), new LongDateStringConverter());
            btnProductBuy.disableProperty().bind(productSchema.tradableProperty().not());
            txtProductBuyAmount.disableProperty().bind(productSchema.tradableProperty().not());
            lblProductTime.textProperty().bindBidirectional(productSchema.priceTimeProperty(), new LongDateStringConverter());
            // Order
            txtProductBuyAmount.textProperty().bindBidirectional(order.amountProperty(), new NumberStringConverter());
            order.priceProperty().bind(productSchema.askProperty());
            lblProductBuyQuantity.textProperty().bind(order.quantityProperty().asString());
            lblProductBuyTotal.textProperty().bind(order.totalProperty().asString());
        }
        else {
            // ProductSchema
            btnProductBuy.disableProperty().unbind();
            txtProductBuyAmount.disableProperty().unbind();
            lblProductName.textProperty().unbindBidirectional(productSchema.productNameProperty());
            lblProductIsin.textProperty().unbindBidirectional(productSchema.isinProperty());
            lblProductAsk.textProperty().unbindBidirectional(productSchema.askProperty());
            lblProductBid.textProperty().unbindBidirectional(productSchema.bidProperty());
            lblProductLastValue.textProperty().unbindBidirectional(productSchema.lastProperty());
            lblProductLastTime.textProperty().unbindBidirectional(productSchema.priceTimeProperty());
            lblProductTime.textProperty().unbindBidirectional(productSchema.priceTimeProperty());
            txtProductBuyAmount.setDisable(true);
            btnProductBuy.setDisable(true);
            // Order
            order.priceProperty().unbind();
            Platform.runLater(() -> order.setPrice(0d));
            txtProductBuyAmount.textProperty().unbindBidirectional(order.amountProperty());
            lblProductBuyQuantity.textProperty().unbindBidirectional(order.quantityProperty());
            lblProductBuyTotal.textProperty().unbindBidirectional(order.totalProperty());
            lblProductBuyQuantity.setText("0");
        }
    }
}