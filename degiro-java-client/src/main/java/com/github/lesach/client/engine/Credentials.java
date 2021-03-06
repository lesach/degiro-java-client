/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lesach.client.engine;

import com.github.lesach.client.log.DLog;
import com.github.lesach.client.utils.DCredentials;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author indiketa
 */
public class Credentials implements DCredentials {

    private Properties props = null;

    public Credentials(File file) {
        props = new Properties();
        try {
            try (InputStream is = new FileInputStream(file)) {
                props.load(is);
            }
        } catch (IOException e) {
            DLog.error("Error loading credentials", e);
        }
    }

    @Override
    public String getUsername() {
        return props.getProperty("username");
    }

    @Override
    public String getPassword() {
        return props.getProperty("password");
    }

}
