package com.whiuk.philip.perfect.mail;


import android.content.Context;

import com.whiuk.philip.perfect.mail.oauth.OAuth2TokenProvider;
import com.whiuk.philip.perfect.mail.ssl.DefaultTrustedSocketFactory;
import com.whiuk.philip.perfect.mail.store.StoreConfig;
import com.whiuk.philip.perfect.mail.transport.smtp.SmtpTransport;
import com.whiuk.philip.perfect.mail.transport.WebDavTransport;

public class TransportProvider {
    private static TransportProvider transportProvider = new TransportProvider();

    public static TransportProvider getInstance() {
        return transportProvider;
    }

    public synchronized Transport getTransport(Context context, StoreConfig storeConfig)
            throws MessagingException {
        String uri = storeConfig.getTransportUri();
        if (uri.startsWith("smtp")) {
            OAuth2TokenProvider oauth2TokenProvider = null;
            return new SmtpTransport(storeConfig, new DefaultTrustedSocketFactory(context),
                    oauth2TokenProvider);
        } else if (uri.startsWith("webdav")) {
            return new WebDavTransport(storeConfig);
        } else {
            throw new MessagingException("Unable to locate an applicable Transport for " + uri);
        }
    }
}
