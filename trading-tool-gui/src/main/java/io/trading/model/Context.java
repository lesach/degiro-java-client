package io.trading.model;

import cat.indiketa.degiro.model.DPortfolioSummary;
import io.trading.controller.MainController;
import io.trading.provider.ConnectionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Context {
    private static final Logger logger = LogManager.getLogger(Context.class);

    private String username;
    private String password;
    private ConnectionProvider connection = null;

    public DPortfolioSummary getPortfolioSummary() {
        return portfolioSummary;
    }

    private DPortfolioSummary portfolioSummary;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Getter
     * @return current connection
     */
    public ConnectionProvider getConnection() {
        if (connection == null)
            connection = new ConnectionProvider(username, password);
        return connection;
    }

    /**
     * Constructor
     */
    public Context() {

    }

    /**
     * Connect to Degiro
     * @return true if OK
     */
    public boolean Connect() {
        connection = new ConnectionProvider(username, password);
        portfolioSummary = connection.getPortfolioSummary();
        return portfolioSummary != null;
    }

}
