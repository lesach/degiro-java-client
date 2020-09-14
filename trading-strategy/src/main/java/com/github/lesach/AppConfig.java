package com.github.lesach;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("credentials")
public class AppConfig
{
    @Value("password")
    public String password;

    @Value("username")
    public String username;

    @Value("jsonPeristenSessionPath")
    public String jsonPeristenSessionPath;
}
