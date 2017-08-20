package com.whiuk.philip.perfect.mail.store.imap;

import com.whiuk.philip.perfect.mail.AuthType;
import com.whiuk.philip.perfect.mail.ConnectionSecurity;
import com.whiuk.philip.perfect.mail.NetworkType;

/**
 * Settings source for IMAP. Implemented in order to remove coupling between {@link ImapStore} and {@link ImapConnection}.
 */
interface ImapSettings {
    String getHost();

    int getPort();

    ConnectionSecurity getConnectionSecurity();

    AuthType getAuthType();

    String getUsername();

    String getPassword();

    String getClientCertificateAlias();

    boolean useCompression(NetworkType type);

    String getPathPrefix();

    void setPathPrefix(String prefix);

    String getPathDelimiter();

    void setPathDelimiter(String delimiter);

    String getCombinedPrefix();

    void setCombinedPrefix(String prefix);
}
