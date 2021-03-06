package com.github.lesach.client.session;

import com.github.lesach.client.DClient;
import com.github.lesach.client.DConfig;
import com.google.gson.annotations.Expose;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author indiketa
 */
@Getter
@Setter
public class DSession {

    @Expose
    protected DConfig config;
    @Expose
    protected DClient client;
    @Expose
    protected String vwdSession;
    @Expose
    protected long lastVwdSessionUsed;
    @Expose
    protected List<BasicClientCookie> cookies;

    public String getJSessionId() {
        String value = null;

        if (cookies != null) {
            int i = 0;
            while (i < cookies.size() && !cookies.get(i).getName().equalsIgnoreCase("JSESSIONID")) {
                i++;
            }

            if (i < cookies.size()) {
                value = cookies.get(i).getValue();
            }
        }

        return value;
    }

    public void clearSession() {
        config = null;
        client = null;
        vwdSession = null;
        cookies = null;
    }

}
