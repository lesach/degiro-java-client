package config;

import java.util.Map;

public class AppConfig {
    static Map<String, String> env = System.getenv();

    /**
     * return Degiro Username to connect to API
     * @return string format
     */
    static public String getDegiroUserName() {
        return env.get("DEGIRO_USERNAME");
    }

    /**
     * return Degiro password to connect to API
     * @return string format
     */
    static public String getDegiroPassword() {
        return env.get("DEGIRO_PASSWORD");
    }
}

