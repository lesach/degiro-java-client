package io.trading.controller;

import cat.indiketa.degiro.model.*;
import cat.indiketa.degiro.utils.DUtils;
import com.google.gson.GsonBuilder;
import io.trading.model.Context;
import io.trading.model.tableview.OrderTableViewSchema;
import io.trading.model.tableview.PositionTableViewSchema;
import io.trading.utils.Format;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private Context context;
    private DProductDescription callProduct;
    private final ObservableList<PositionTableViewSchema> positionsData = FXCollections.observableArrayList(PositionTableViewSchema.extractor());
    private final ObservableList<OrderTableViewSchema> ordersData = FXCollections.observableArrayList();

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
        context = new Context();
        // Positions table
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
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                    }
                };
        colPositionSell.setCellFactory(positionCellFactory);

        tabPositions.setItems(positionsData);
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
                                logger.info("Delete order: " + o.getProduct() + " -> " + Format.formatBigDecimal(o.getPrice()));
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };
        colOrderDelete.setCellFactory(orderCellFactory);
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
        return  result;
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
        FilteredList<PositionTableViewSchema> list = this.positionsData.filtered(t -> t.getId() == 123456L);
        if (list.isEmpty()) {
            PositionTableViewSchema s = new PositionTableViewSchema(123456L,
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
        }
        else {
            PositionTableViewSchema s = list.get(0);
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
        //updatePositions();
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
            shpConnected.setFill(Paint.valueOf("#55FF55"));
            if (context.Connect()) {
                txtUser.getStyleClass().remove("error");
                txtPassword.getStyleClass().remove("error");
                // Register a price update listener (called on price update)
                context.setPriceListener(new DPriceListener() {
                    @Override
                    public void priceChanged(DPrice price) {
                        logger.info("Price changed: " + new GsonBuilder().setPrettyPrinting().create().toJson(price));
                        if (price.getIssueId().equals(callProduct.getVwdId())) {
                            // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                            Platform.runLater(
                                    () -> {
                                        lblCallProductAsk.setText(Format.formatBigDecimal(price.getAsk()));
                                        lblCallProductBid.setText(Format.formatBigDecimal(price.getBid()));
                                        lblCallProductLastValue.setText(Format.formatBigDecimal(price.getLast()));
                                        lblCallProductLastTime.setText(Format.formatDate(price.getLastTime()));
                                        lblCallProductTime.setText(Format.formatDate(new Date()));
                                        BigDecimal amout = Format.parseBigDecimal(txtCallProductBuyAmount.getText());
                                        if (amout == null || price.getAsk() == null)
                                            lblCallProductBuyQuantity.setText("0");
                                        else {
                                            long quantity = amout.divide(new BigDecimal(price.getAsk()), RoundingMode.FLOOR).longValueExact();
                                            lblCallProductBuyQuantity.setText(Long.toString(quantity));
                                        }

                                    }
                            );
                        }
                    }
                });
            }
            else {
                txtUser.getStyleClass().add("error");
                txtPassword.getStyleClass().add("error");
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
            txtCallProductBuyAmount.setDisable(callProduct.isTradable());
            btnCallProductBuy.setDisable(callProduct.isTradable());
        }
    }

    /**
     * Search Call product
     * @param event trigger
     */
    @FXML protected void handleCallProductSearchButtonAction(ActionEvent event) {
        logger.info("Call Product Search button pressed");
        if (checkCallProductSearch()) {
            List<DProductDescription> products = this.context.searchProducts(txtCallProductSearch.getText());
            if (products != null && products.size() == 1)
                callProduct = products.get(0);
            else
                callProduct = null;
        }
        else
            callProduct = null;
        displayCallProduct();
    }

    /**
     * Display positionsData
     */
    private void updatePositions() {
        DPortfolioProducts produtcs = this.context.getPortfolio();
        produtcs.getActive().forEach(p -> {
            FilteredList<PositionTableViewSchema> list = this.positionsData.filtered(t -> t.getId() == p.getId());
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
            }
            else {
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
    }

    /**
     * Display ordersData
     */
    private void updateOrders() {
        List<DOrder> orders = this.context.getOrders();
        orders.forEach(o -> {
            FilteredList<OrderTableViewSchema> list = this.ordersData.filtered(t -> t.getId().equals(o.getId()));
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

                DNewOrder order = new DNewOrder(DOrderAction.SELL,
                        DOrderType.LIMITED,
                        DOrderTime.DAY,
                        callProduct.getId(),
                        Long.parseLong(lblCallProductBuyQuantity.getText()),
                        Format.parseBigDecimal(lblCallProductAsk.getText()),
                        null);

                DOrderConfirmation confirmation = context.checkOrder(order);

                if (!confirmation.getConfirmationId().isEmpty()) {
                    DPlacedOrder placed = context.confirmOrder(order, confirmation);
                    if (placed.getStatus() != 0) {
                        throw new RuntimeException("Order not placed: " + placed.getStatusText());
                    }
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

}