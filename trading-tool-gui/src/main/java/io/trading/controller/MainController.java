package io.trading.controller;

import cat.indiketa.degiro.model.*;
import com.google.gson.GsonBuilder;
import io.trading.config.AppConfig;
import io.trading.model.Context;
import io.trading.model.Product;
import io.trading.model.tableview.OrderTableViewSchema;
import io.trading.model.tableview.PositionTableViewSchema;
import io.trading.utils.Format;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private final Context context = new Context();
    private Product callProduct;
    private final Map<Long, Product> products = new HashMap<>();
    private List<String> subscribedProducts = new ArrayList<>();
    private ObservableList<PositionTableViewSchema> positionsData;
    PositionsScheduledService positionsScheduledService;

    private ObservableList<OrderTableViewSchema> ordersData = FXCollections.observableArrayList();
    OrdersScheduledService ordersScheduledService;

    // Credentials pane
    @FXML private TextField txtUser;
    @FXML private TextField txtPassword;
    @FXML private Button btnConnect;
    @FXML private Circle shpConnected;

    // Client pane
    @FXML private Button btnPositionsRefresh;
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
        colPositionProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colPositionPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        colPositionQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPositionPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPositionCurrency.setCellValueFactory(new PropertyValueFactory<>("currency"));
        colPositionTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPositionDailyPL.setCellValueFactory(new PropertyValueFactory<>("dailyPL"));
        colPositionDailyVariation.setCellValueFactory(new PropertyValueFactory<>("dailyVariation"));
        colPositionTotalPL.setCellValueFactory(new PropertyValueFactory<>("totalPL"));
        colPositionTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPositionSell.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        Callback<TableColumn<PositionTableViewSchema, String>,
                TableCell<PositionTableViewSchema, String>> positionCellFactory
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
                                        logger.info("Sell product: " + p.getProduct() + " -> " + Format.formatBigDecimal(p.getPrice()));
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
        colPositionSell.setCellFactory(positionCellFactory);
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
                // subscribe to price update
                //context.subscribeToPrice(Long.toString(s.getId()));
            }
        });

        // Orders table
        colOrderBuyOrSell.setCellValueFactory(new PropertyValueFactory<>("buyOrSell"));
        colOrderProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
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
                                logger.info("Delete order: " + o.getProduct() + " -> " + Format.formatBigDecimal(o.getLimit()));

                                DPlacedOrder deleted = context.deleteOrder(o.getId()); // orderId obtained in getOrders()
                                if (deleted.getStatus() != 0) {
                                    String tmp = o.getProduct();
                                    o.setProduct("");
                                    o.setError(true);
                                    o.setProduct(tmp);
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
                // subscribe to price update
                //context.subscribeToPrice(Long.toString(s.getId()));
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
     * Connect to API
     * @param event trigger
     */
    @FXML protected void handlePositionsRefreshButtonAction(ActionEvent event) {
        logger.info("Refresh Positions button pressed");
        /*if (AppConfig.getTest()) {
            FilteredList<PositionTableViewSchema> list = this.positionsData.filtered(t -> t.getId() == 123456L);
            PositionTableViewSchema s;
            if (list.isEmpty()) {
                s = new PositionTableViewSchema(123456L,
                        "Product",
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
            } else {
                s = list.get(0);
                s.update(123456L,
                        "Product",
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
            // subscribe to price update
            context.subscribeToPrice(Long.toString(s.getId()));
        }
        else
            updatePositions();*/
    }

    /**
     * Update displayed prices
     * @param price
     */
    private void updatePrices(DPrice price) {
        if (callProduct != null) {
            // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
            Platform.runLater(
                    () -> {
                        lblCallProductAsk.setText(Format.formatBigDecimal(callProduct.getAsk()));
                        lblCallProductBid.setText(Format.formatBigDecimal(callProduct.getBid()));
                        lblCallProductLastValue.setText(Format.formatBigDecimal(callProduct.getLast()));
                        lblCallProductLastTime.setText(Format.formatDate(callProduct.getPriceTime()));
                        lblCallProductTime.setText(Format.formatDate(new Date()));
                        computeCallQuantityandTotal();
                    }
            );
        }
        FilteredList<PositionTableViewSchema> positionList = positionsData.filtered(t -> Long.toString(t.getId()).equals(price.getIssueId()));
        if (!positionList.isEmpty()) {
            positionList.get(0).update(price.getAsk(), price.getBid());
        }
        FilteredList<OrderTableViewSchema> orderList = ordersData.filtered(t -> t.getId().equals(price.getIssueId()));
        if (!orderList.isEmpty()) {
            orderList.get(0).update(price.getAsk(), price.getBid());
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
                        Optional<Product> product = products.values().stream().filter(p -> p.getVwdId().equals(price.getIssueId())).findFirst();
                        product.ifPresent(p -> p.adopt(price));
                        updatePrices(price);
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
    private void displayCallProduct() {
        if (callProduct == null) {
            lblCallProductName.setText("");
            lblCallProductAsk.setText("");
            lblCallProductBid.setText("");
            lblCallProductBuyQuantity.setText("0");
            context.clearPriceSubscriptions();
            txtCallProductBuyAmount.setDisable(true);
            btnCallProductBuy.setDisable(true);
        }
        else {
            lblCallProductName.setText(callProduct.getName() + "(" + callProduct.getSymbol() + " / " + callProduct.getIsin() + ")");
            lblCallProductAsk.setText("");
            lblCallProductBid.setText("");
            context.subscribeToPrice(callProduct.getVwdId());
            lblCallProductBuyQuantity.setText("0");
            txtCallProductBuyAmount.setDisable(!callProduct.isTradable());
            btnCallProductBuy.setDisable(!callProduct.isTradable());
        }
    }

    /**
     * Search Call product
     * @param event trigger
     */
    @FXML protected void handleCallProductSearchButtonAction(ActionEvent event) {
        logger.info("Call Product Search button pressed");
        if (checkCallProductSearch()) {
            List<DProductDescription> descriptions = this.context.searchProducts(txtCallProductSearch.getText());
            if (descriptions != null && descriptions.size() == 1) {
                mergeDescriptionProducts(descriptions);
                callProduct = products.get(descriptions.get(0).getId());
            }
            else
                callProduct = null;
        }
        else
            callProduct = null;
        displayCallProduct();
    }

    /**
     * Create an order to Buy Call Product
     * @param event trigger
     */
    @FXML protected void handleCallProductBuyButtonAction(ActionEvent event) {
        logger.info("Call Product Buy button pressed");
        FilteredList<PositionTableViewSchema> list = this.positionsData.filtered(t -> t.getId() == 123456L);
        if (this.callProduct != null) {
            if (this.callProduct.isTradable())
            {
                logger.info("Creating Buy order");
                // Generate a new order. Signature:
                // public DNewOrder(DOrderAction action, DOrderType orderType, DOrderTime timeType, long productId, long size, BigDecimal limitPrice, BigDecimal stopPrice)
                if (AppConfig.getTest()) {
                    OrderTableViewSchema order = new OrderTableViewSchema(
                            "Order1",
                            DOrderAction.SELL.toString(),
                            callProduct.getName(),
                            DOrderType.LIMITED.toString(),
                            Format.parseBigDecimal(lblCallProductAsk.getText()).doubleValue(),
                            "EUR",
                            Long.parseLong(lblCallProductBuyQuantity.getText())
                            );
                    context.subscribeToPrice(callProduct.getVwdId());
                    ordersData.add(order);
                }
                else {
                    DNewOrder order = new DNewOrder(DOrderAction.SELL,
                            DOrderType.LIMITED,
                            DOrderTime.DAY,
                            callProduct.getId(),
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
    private void computeCallQuantityandTotal() {
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
        logger.info("Refresh Positions button pressed");

    }


    /**
     *
     * @param newProducts
     * @return
     */
    private boolean mergePortfolioProducts(List<DPortfolioProducts.DPortfolioProduct> newProducts) {
        boolean oneRegistered = false;
        if (newProducts != null) {
            for (DPortfolioProducts.DPortfolioProduct product : newProducts) {
                if (products.containsKey(product.getId())) {
                    products.get(product.getId()).adopt(product);
                } else {
                    registerProduct(product);
                    oneRegistered = true;
                }
            }
        }
        return oneRegistered;
    }


    /**
     *
     * @param newProducts
     * @return
     */
    private boolean mergeDescriptionProducts(List<DProductDescription> newProducts) {
        boolean oneRegistered = false;
        if (newProducts != null) {
            for (DProductDescription product : newProducts) {
                if (products.containsKey(product.getId())) {
                    products.get(product.getId()).adopt(product);
                } else {
                    registerProduct(product);
                    oneRegistered = true;
                }
            }
        }
        return oneRegistered;
    }
    /**
     *
     * @param add
     */
    private void registerProduct(DPortfolioProducts.DPortfolioProduct add) {
        Product pro = new Product();
        pro.adopt(add);
        products.put(add.getId(), pro);
    }

    /**
     *
     * @param add
     */
    private void registerProduct(DProductDescription add) {
        Product pro = new Product();
        pro.adopt(add);
        products.put(add.getId(), pro);
    }
}