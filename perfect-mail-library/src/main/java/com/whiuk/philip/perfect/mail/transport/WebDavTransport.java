
package com.whiuk.philip.perfect.mail.transport;

import com.whiuk.philip.perfect.mail.MailLibrary;
import com.whiuk.philip.perfect.mail.Message;
import com.whiuk.philip.perfect.mail.MessagingException;
import com.whiuk.philip.perfect.mail.ServerSettings;
import com.whiuk.philip.perfect.mail.Transport;
import com.whiuk.philip.perfect.mail.store.StoreConfig;
import com.whiuk.philip.perfect.mail.store.webdav.WebDavHttpClient;
import com.whiuk.philip.perfect.mail.store.webdav.WebDavStore;
import timber.log.Timber;

import java.util.Collections;

public class WebDavTransport extends Transport {

    /**
     * Decodes a WebDavTransport URI.
     *
     * <p>
     * <b>Note:</b> Everything related to sending messages via WebDAV is handled by
     * {@link WebDavStore}. So the transport URI is the same as the store URI.
     * </p>
     */
    public static ServerSettings decodeUri(String uri) {
        return WebDavStore.decodeUri(uri);
    }

    /**
     * Creates a WebDavTransport URI.
     *
     * <p>
     * <b>Note:</b> Everything related to sending messages via WebDAV is handled by
     * {@link WebDavStore}. So the transport URI is the same as the store URI.
     * </p>
     */
    public static String createUri(ServerSettings server) {
        return WebDavStore.createUri(server);
    }


    private WebDavStore store;

    public WebDavTransport(StoreConfig storeConfig) throws MessagingException {
        store = new WebDavStore(storeConfig, new WebDavHttpClient.WebDavHttpClientFactory());

        if (MailLibrary.isDebug())
            Timber.d(">>> New WebDavTransport creation complete");
    }

    @Override
    public void open() throws MessagingException {
        if (MailLibrary.isDebug())
            Timber.d( ">>> open called on WebDavTransport ");

        store.getHttpClient();
    }

    @Override
    public void close() {
    }

    @Override
    public void sendMessage(Message message) throws MessagingException {
        store.sendMessages(Collections.singletonList(message));
    }
}
