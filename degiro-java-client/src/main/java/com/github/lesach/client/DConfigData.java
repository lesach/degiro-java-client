package com.github.lesach.client;


import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author indiketa
 */
@Getter
@Setter
public class DConfigData {

    /**
     *         "tradingUrl": "https://trader.degiro.nl/trading/secure/",
     *         "paUrl": "https://trader.degiro.nl/pa/secure/",
     *         "reportingUrl": "https://trader.degiro.nl/reporting/secure/",
     *         "paymentServiceUrl": "https://trader.degiro.nl/payments/",
     *         "productSearchUrl": "https://trader.degiro.nl/product_search/secure/",
     *         "dictionaryUrl": "https://trader.degiro.nl/product_search/config/dictionary/",
     *         "productTypesUrl": "https://trader.degiro.nl/product_search/config/productTypes/",
     *         "companiesServiceUrl": "https://trader.degiro.nl/dgtbxdsservice/",
     *         "i18nUrl": "https://trader.degiro.nl/i18n/",
     *         "vwdQuotecastServiceUrl": "https://trader.degiro.nl/vwd-quotecast-service/",
     *         "vwdNewsUrl": "https://solutions.vwdservices.com/customers/degiro.nl/news-feed/api/",
     *         "vwdGossipsUrl": "https://solutions.vwdservices.com/customers/degiro.nl/news-feed/api/",
     *         "taskManagerUrl": "https://trader.degiro.nl/taskmanager/",
     *         "refinitivNewsUrl": "https://trader.degiro.nl/dgtbxdsservice/newsfeed/v2",
     *         "refinitivAgendaUrl": "https://trader.degiro.nl/dgtbxdsservice/agenda/v2",
     *         "refinitivCompanyProfileUrl": "https://trader.degiro.nl/dgtbxdsservice/company-profile/v2",
     *         "refinitivCompanyRatiosUrl": "https://trader.degiro.nl/dgtbxdsservice/company-ratios",
     *         "refinitivFinancialStatementsUrl": "https://trader.degiro.nl/dgtbxdsservice/financial-statements",
     *         "refinitivClipsUrl": "https://trader.degiro.nl/refinitiv-insider-proxy/secure/",
     *         "landingPath": "/trader/",
     *         "betaLandingPath": "/beta-trader/",
     *         "mobileLandingPath": "/trader/",
     *         "loginUrl": "https://trader.degiro.nl/login/fr",
     *         "sessionId": "EE8D26C32435E074B0DEC73C04CD1901.prod_b_113_5",
     *         "clientId": 184306
     */
    private String tradingUrl;
    private Integer clientId;
    private String i18nUrl;
    private String paymentServiceUrl;
    private String reportingUrl;
    private String paUrl;
    private String vwdQuotecastServiceUrl;
    private String sessionId;
    private String productSearchUrl;
    private String dictionaryUrl;
    private String taskManagerUrl;
    private String loginUrl;
    private String vwdGossipsUrl;
    private String companiesServiceUrl;
    private String productTypesUrl;
    private String vwdNewsUrl;
    private String refinitivNewsUrl;
    private String refinitivAgendaUrl;
    private String refinitivCompanyProfileUrl;
    private String refinitivCompanyRatiosUrl;
    private String refinitivFinancialStatementsUrl;
    private String refinitivClipsUrl;
    private String landingPath;
    private String betaLandingPath;
    private String mobileLandingPath;
    
}
