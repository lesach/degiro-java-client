package io.trading.controller;

import cat.indiketa.degiro.model.DPortfolioSummary;
import cat.indiketa.degiro.model.DPrice;
import cat.indiketa.degiro.model.DPriceListener;
import cat.indiketa.degiro.model.DProductDescription;
import com.google.gson.GsonBuilder;
import io.trading.model.Context;
import io.trading.model.tableview.OrderTableViewSchema;
import io.trading.model.tableview.PositionTableViewSchema;
import io.trading.utils.Format;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private Context context;
    private DProductDescription callProduct;
    private final ObservableList<PositionTableViewSchema> positions = FXCollections.observableArrayList();
    private final ObservableList<OrderTableViewSchema> orders = FXCollections.observableArrayList();


    // Credentials pane
    @FXML private TextField txtUser;
    @FXML private TextField txtPassword;
    @FXML private Button btnConnect;
    @FXML private Circle shpConnected;

    // Position pane
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

    // Order table
    @FXML TableView<OrderTableViewSchema> tabOrders;
    @FXML TableColumn<PositionTableViewSchema, String> colOrderBuyOrSell;
    @FXML TableColumn<PositionTableViewSchema, String> colOrderProduct;
    @FXML TableColumn<PositionTableViewSchema, String> colOrderType;
    @FXML TableColumn<PositionTableViewSchema, Double> colOrderLimit;
    @FXML TableColumn<PositionTableViewSchema, Double> colOrderQuantity;
    @FXML TableColumn<PositionTableViewSchema, String> colOrderCurrency;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Controller loading...");
        context = new Context();
        colPositionProduct.setCellValueFactory(new PropertyValueFactory<>("Product"));
        tabOrders.setItems(orders);
        tabPositions.setItems(positions);
        logger.info("Controller is now loaded");
    }


    /**
     * Check if user and password are not empty
     * @return true if OK
     */
    private boolean checkCredentials() {
        boolean result = true;
        // User name
        String username = txtUser.getText();
        if (username.isEmpty()) {
            txtUser.getStyleClass().add("error");
            result = false;
        }
        else
            txtUser.getStyleClass().remove("error");

        // Password
        String password = txtPassword.getText();
        if (password.isEmpty()) {
            txtPassword.getStyleClass().add("error");
            result = false;
        }
        else
            txtPassword.getStyleClass().remove("error");

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
        boolean result = true;
        // Search
        String search = txtCallProductSearch.getText();
        if (search.isEmpty()) {
            txtCallProductSearch.getStyleClass().add("error");
            result = false;
        }
        else
            txtCallProductSearch.getStyleClass().remove("error");
        return  result;
    }

    /**
     * Fill Porfolio panel
     */
    private void displayCallProduct() {
        if (callProduct == null) {
            lblCallProductName.setText("");
            lblCallProductAsk.setText("");
            lblCallProductBid.setText("");
            context.clearPriceSubscriptions();
        }
        else {
            lblCallProductName.setText(callProduct.getName() + "(" + callProduct.getSymbol() + " / " + callProduct.getIsin() + ")");
            lblCallProductAsk.setText("");
            lblCallProductBid.setText("");
            context.subscribeToPrice(callProduct.getVwdId());
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

}