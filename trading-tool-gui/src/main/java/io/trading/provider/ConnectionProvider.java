package io.trading.provider;

import cat.indiketa.degiro.DeGiro;
import cat.indiketa.degiro.DeGiroFactory;
import cat.indiketa.degiro.model.DPortfolioSummary;
import cat.indiketa.degiro.model.DProductDescription;
import cat.indiketa.degiro.model.DProductSearch;
import cat.indiketa.degiro.model.DProductType;
import cat.indiketa.degiro.utils.DCredentials;
import io.trading.model.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ConnectionProvider {
    private static final Logger logger = LogManager.getLogger(ConnectionProvider.class);
    private DeGiro degiro;

    /**
     * Costructor
     * @param username user
     * @param password password
     */
    public ConnectionProvider(String username, String password) {
        DCredentials creds = new DCredentials() {
            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getPassword() {
                return password;
            }
        };
        this.degiro = DeGiroFactory.newInstance(creds);
    }


    /**
     * Return true if connection is established
     * @return boolean
     */
    public DPortfolioSummary getPortfolioSummary() {
        try {
            return this.degiro.getPortfolioSummary();
        }
        catch (Exception e) {
            logger.error("ERROR in getPortfolioSummary", e);
            return null;
        }
    }

    /**
     * Text to find
     * @param text to find
     * @return list of corresponding products
     */
    public List<DProductDescription> searchProducts(String text) {
        try {
            DProductSearch search = this.degiro.searchProducts(text, DProductType.ALL, 10, 0);
            return search.getProducts();
        }
        catch (Exception e) {
            logger.error("ERROR in searchProducts", e);
            return null;
        }
    }
}
