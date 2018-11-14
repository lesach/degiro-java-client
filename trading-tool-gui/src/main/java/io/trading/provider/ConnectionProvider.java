package io.trading.provider;

import cat.indiketa.degiro.DeGiro;
import cat.indiketa.degiro.DeGiroFactory;
import cat.indiketa.degiro.utils.DCredentials;

public class ConnectionProvider {
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
}
