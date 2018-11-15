package io.trading.controller;

import cat.indiketa.degiro.model.DPortfolioSummary;
import io.trading.model.Context;
import io.trading.utils.Format;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private Context context;

    @FXML private TextField txtUser;
    @FXML private TextField txtPassword;
    @FXML private Circle shpConnected;
    @FXML private TextField txtName;
    @FXML private TextField txtTotal;
    @FXML private TextField txtShares;
    @FXML private TextField txtCash;
    @FXML private TextField txtAvailable;
    @FXML private TextField txtVariation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Controller loading...");
            context = new Context();
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
     * Fill Porfolio panel
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

}