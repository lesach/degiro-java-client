package com.github.lesach.config;

import com.github.lesach.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UIConfig {

    @Autowired
    public AppConfig appConfig;

    /**
     * return Degiro Username to connect to API
     * @return string format
     */
    public String getDegiroUserName() {
        return appConfig.username;
    }

    /**
     * return Degiro password to connect to API
     * @return string format
     */
    public String getDegiroPassword() {
        return appConfig.password;
    }

    /**
     * return Degiro password to connect to API
     * @return string format
     */
    public String getDegiroPersitentSessionPath() {
        return appConfig.jsonPeristenSessionPath;
    }


    /**
     * return Test mode
     * @return boolean Test mode
     */
    public boolean getTest() {
        return appConfig.test.toLowerCase().equals("true");
    }

    /**
     * return Test mode
     * @return boolean Test mode
     */
    public int getPriceRefreshErrorThreshold() {
        return appConfig.priceRefreshErrorThreshold;
    }
}

