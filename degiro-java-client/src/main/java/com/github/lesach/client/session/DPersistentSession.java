package com.github.lesach.client.session;

import com.github.lesach.client.DConfig;
import com.github.lesach.client.log.DLog;
import com.github.lesach.client.DClient;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author indiketa
 */
public class DPersistentSession extends DSession {

    private volatile File file;
    private volatile Gson gson;

    public DPersistentSession(String file) {
        this(Strings.isNullOrEmpty(file) ? null : new File(file));
    }

    public DPersistentSession(File file) {
        this.file = file;
        gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.VOLATILE, Modifier.STATIC).create();
        loadSession();
    }

    private void loadSession() {

        if (file == null) { // early exit
            return;
        }

        try {
            if (file.exists()) {
                try (FileReader fr = new FileReader(file)) {
                    DSession ds = gson.fromJson(fr, DSession.class);
                    if (ds != null) {
                        config = ds.config;
                        client = ds.client;
                        vwdSession = ds.vwdSession;
                        cookies = ds.cookies;
                        lastVwdSessionUsed = ds.lastVwdSessionUsed;
                        DLog.info("Permanent session storage loaded (" + file.length() + " bytes).");
                    }
                }
            }
        } catch (IOException e) {
            DLog.error("Error while loading persistent session data", e);
        }
    }

    private void saveSession() {

        if (file == null) { // early exit
            return;
        }

        try {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(gson.toJson(this));
            }
            DLog.info("Permanent session storage updated (" + file.length() + " bytes).");
        } catch (IOException e) {
            DLog.error("Error while saving persistent session data", e);
        }

    }

    @Override
    public void setCookies(List<BasicClientCookie> cookies) {
        super.setCookies(cookies);
        saveSession();
    }

    @Override
    public void setVwdSession(String vwdSession) {
        super.setVwdSession(vwdSession);
        saveSession();
    }

    @Override
    public void setClient(DClient client) {
        super.setClient(client);
        saveSession();
    }

    @Override
    public void setConfig(DConfig config) {
        super.setConfig(config);
        saveSession();
    }

    @Override
    public void setLastVwdSessionUsed(long lastVwdSessionUsed) {
        super.setLastVwdSessionUsed(lastVwdSessionUsed);
        saveSession();
    }

    
}
