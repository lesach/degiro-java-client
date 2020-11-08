package com.github.lesach.client.http;

import com.github.lesach.client.log.DLog;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.HttpClientConnectionManager;

/**
 *
 * @author indiketa
 */
class DInactiveConnectionManager extends Thread {

    private final HttpClientConnectionManager connectionManager;
    private volatile boolean shutdown;

    public DInactiveConnectionManager(HttpClientConnectionManager connectionManager) {
        super("INACTIVE-CONNECTION-MANAGER");
        setDaemon(true);
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        while (!shutdown) {
            synchronized (this) {
                try {
                    wait(5000);
                    connectionManager.closeExpiredConnections();
                    connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
                } catch (Exception e) {
                    DLog.error("Exception closing expired connections", e);
                }
            }
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }

}
