package com.whiuk.philip.perfect.mailstore;

import com.whiuk.philip.perfect.mail.Message;

public interface MessageRemovalListener {
    public void messageRemoved(Message message);
}
