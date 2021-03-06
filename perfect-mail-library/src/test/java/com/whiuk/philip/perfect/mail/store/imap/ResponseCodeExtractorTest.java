package com.whiuk.philip.perfect.mail.store.imap;


import org.junit.Test;

import static com.whiuk.philip.perfect.mail.store.imap.ImapResponseHelper.createImapResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class ResponseCodeExtractorTest {
    @Test
    public void getResponseCode_withResponseCode() throws Exception {
        ImapResponse imapResponse = createImapResponse("x NO [AUTHENTICATIONFAILED] No sir");

        String result = ResponseCodeExtractor.getResponseCode(imapResponse);

        assertEquals("AUTHENTICATIONFAILED", result);
    }

    @Test
    public void getResponseCode_withoutResponseCode() throws Exception {
        ImapResponse imapResponse = createImapResponse("x NO Authentication failed");

        String result = ResponseCodeExtractor.getResponseCode(imapResponse);

        assertNull(result);
    }

    @Test
    public void getResponseCode_withoutSingleItemResponse() throws Exception {
        ImapResponse imapResponse = createImapResponse("x NO");

        String result = ResponseCodeExtractor.getResponseCode(imapResponse);

        assertNull(result);
    }
}
