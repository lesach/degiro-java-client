package model;

import provider.ConnectionProvider;

public class Context {
    private String username;
    private String password;
    private ConnectionProvider connection = null;

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
     * @param username API username
     * @param password API password
     */
    public Context(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
