package com.whiuk.philip.perfect.account;


import com.whiuk.philip.perfect.Account.DeletePolicy;
import com.whiuk.philip.perfect.mail.ConnectionSecurity;
import com.whiuk.philip.perfect.mail.ServerSettings.Type;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AccountCreatorTest {

    @Test
    public void getDefaultDeletePolicy_withImap_shouldReturn_ON_DELETE() {
        DeletePolicy result = AccountCreator.getDefaultDeletePolicy(Type.IMAP);

        assertEquals(DeletePolicy.ON_DELETE, result);
    }

    @Test
    public void getDefaultDeletePolicy_withPop3_shouldReturn_NEVER() {
        DeletePolicy result = AccountCreator.getDefaultDeletePolicy(Type.POP3);

        assertEquals(DeletePolicy.NEVER, result);
    }

    @Test
    public void getDefaultDeletePolicy_withWebDav_shouldReturn_ON_DELETE() {
        DeletePolicy result = AccountCreator.getDefaultDeletePolicy(Type.WebDAV);

        assertEquals(DeletePolicy.ON_DELETE, result);
    }

    @Test
    public void getDefaultPort_withNoConnectionSecurityAndImap_shouldReturnDefaultPort() {
        int result = AccountCreator.getDefaultPort(ConnectionSecurity.NONE, Type.IMAP);

        assertEquals(Type.IMAP.defaultPort, result);
    }

    @Test
    public void getDefaultPort_withStartTlsAndImap_shouldReturnDefaultPort() {
        int result = AccountCreator.getDefaultPort(ConnectionSecurity.STARTTLS_REQUIRED, Type.IMAP);

        assertEquals(Type.IMAP.defaultPort, result);
    }

    @Test
    public void getDefaultPort_withTlsAndImap_shouldReturnDefaultTlsPort() {
        int result = AccountCreator.getDefaultPort(ConnectionSecurity.SSL_TLS_REQUIRED, Type.IMAP);

        assertEquals(Type.IMAP.defaultTlsPort, result);
    }
}
