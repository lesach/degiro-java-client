package com.github.lesach.client.http;

import com.github.lesach.client.session.DSession;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

/**
 *
 * @author indiketa
 */
public class DCommunication extends DHttpManager {

    private final Gson gson;
    private final BasicHttpContext context;
    // Extracted from: https://charting.vwdservices.com/hchart/v1/deGiro/api.js?culture=en&userToken=XXXXXX&tz=Europe/Madrid
    private static final String CHARTING_URL = "https://charting.vwdservices.com/hchart/v1/deGiro/data.js";

    public DCommunication(DSession session) {
        super(session);
        this.gson = new Gson();
        this.context = new BasicHttpContext();
    }


    public DResponse getUrlData(String base, String uri, Object data) throws IOException {
        return getUrlData(base, uri, data, null, null);
    }

    public DResponse getUrlData(String base, String uri, Object data, List<Header> headers) throws IOException {
        return getUrlData(base, uri, data, headers, null);
    }

    public DResponse getUrlData(String base, String uri, Object data, List<Header> headers, final String method) throws IOException {

        String url = base + uri;
        HttpRequestBase request;

        if (data == null) {
            request = new HttpRequestBase() {
                @Override
                public String getMethod() {
                    return Strings.isNullOrEmpty(method) ? "GET" : method;
                }
            };
        } else {
            request = new HttpEntityEnclosingRequestBase() {
                @Override
                public String getMethod() {
                    return Strings.isNullOrEmpty(method) ? "POST" : method;
                }
            };
            request.addHeader("Content-Type", "application/json");
            String jsonData = gson.toJson(data);
            ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(jsonData));
        }

        request.setURI(URI.create(url));

        if (headers != null) {
            for (Header header : headers) {
                request.addHeader(header);
            }
        }

        DResponse dResponse;

        try (CloseableHttpResponse response = httpClient.execute(request, context)) {
            dResponse = new DResponse();
            dResponse.setStatus(response.getStatusLine().getStatusCode());
            dResponse.setText(CharStreams.toString(new InputStreamReader(response.getEntity().getContent(), Charsets.UTF_8)));
            dResponse.setMethod(request.getMethod());
            dResponse.setUrl(url);
        }

        return dResponse;

    }

    public DResponse getData(DSession session, String params, Object data) throws IOException {
        return getUrlData(session.getConfig().getData().getTradingUrl() + "v5/update/" + session.getClient().getData().getIntAccount() + ";jsessionid=" + session.getJSessionId(), "?" + params, data);
    }


    public DResponse getGraph(DSession session,
                              String parameters,
                              String resolution) throws IOException {
            /*
    requestid:  1
    resolution: PT1S
    culture:    en-US
    period:     P1D
    series:     issueid:280172443
    format:     json
    callback:   vwd.hchart.seriesRequestManager.sync_response
    userToken:  91940
    tz:         Europe/Madrid
             */
        return getUrlData(CHARTING_URL,
                "?requestid=1&resolution=" + resolution + "&culture=en-US&" + parameters + "&format=json&userToken=" + session.getConfig().getData().getClientId() + "&tz=Europe%2FAmsterdam",
                null);
    }

}
