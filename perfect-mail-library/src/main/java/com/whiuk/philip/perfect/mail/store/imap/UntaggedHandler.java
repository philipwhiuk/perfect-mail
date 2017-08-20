package com.whiuk.philip.perfect.mail.store.imap;

import java.io.IOException;

interface UntaggedHandler {
    void handleAsyncUntaggedResponse(ImapResponse response) throws IOException;
}
