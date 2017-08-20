package com.whiuk.philip.perfect.mail.store.imap;


import java.io.IOException;
import java.util.List;

import com.whiuk.philip.perfect.mail.MessagingException;


interface ImapSearcher {
    List<ImapResponse> search() throws IOException, MessagingException;
}
