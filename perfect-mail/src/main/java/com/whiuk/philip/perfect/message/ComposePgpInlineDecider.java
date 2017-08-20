package com.whiuk.philip.perfect.message;


import java.util.List;

import com.whiuk.philip.perfect.crypto.MessageDecryptVerifier;
import com.whiuk.philip.perfect.mail.Message;
import com.whiuk.philip.perfect.mail.Part;


public class ComposePgpInlineDecider {
    public boolean shouldReplyInline(Message localMessage) {
        // TODO more criteria for this? maybe check the User-Agent header?
        return messageHasPgpInlineParts(localMessage);
    }

    private boolean messageHasPgpInlineParts(Message localMessage) {
        List<Part> inlineParts = MessageDecryptVerifier.findPgpInlineParts(localMessage);
        return !inlineParts.isEmpty();
    }
}
