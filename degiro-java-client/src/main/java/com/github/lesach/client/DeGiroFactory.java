package com.github.lesach.client;

import com.github.lesach.client.session.DSession;
import com.github.lesach.client.session.DSessionExpiredRetryProxy;
import com.github.lesach.client.utils.DCredentials;

/**
 *
 * @author indiketa
 */
public class DeGiroFactory {

    public static DeGiro newInstance(DCredentials credentials) {
        return newInstance(credentials, new DSession());
    }

    public static DeGiro newInstance(DCredentials credentials, DSession session) {
        DeGiroImpl dmanager = new DeGiroImpl(credentials, session);
        return DSessionExpiredRetryProxy.newInstance(dmanager);
    }

}
