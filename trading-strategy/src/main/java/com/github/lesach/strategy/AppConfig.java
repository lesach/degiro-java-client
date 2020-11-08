package com.github.lesach.strategy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class AppConfig
{
    @Value("${trading.tool.password}")
    public String password;

    @Value("${trading.tool.username}")
    public String username;

    @Value("${trading.tool.jsonPeristenSessionPath}")
    public String jsonPeristenSessionPath;

    @Value("${trading.tool.test}")
    public String test;

    @Value("${trading.tool.priceRefreshErrorThreshold}")
    public Integer priceRefreshErrorThreshold;

}
