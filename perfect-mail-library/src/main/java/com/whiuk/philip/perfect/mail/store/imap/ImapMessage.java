package com.whiuk.philip.perfect.mail.store.imap;


import java.util.Collections;

import com.whiuk.philip.perfect.mail.Flag;
import com.whiuk.philip.perfect.mail.Folder;
import com.whiuk.philip.perfect.mail.MessagingException;
import com.whiuk.philip.perfect.mail.internet.MimeMessage;


class ImapMessage extends MimeMessage {
    ImapMessage(String uid, Folder folder) {
        this.mUid = uid;
        this.mFolder = folder;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public void setFlagInternal(Flag flag, boolean set) throws MessagingException {
        super.setFlag(flag, set);
    }

    @Override
    public void setFlag(Flag flag, boolean set) throws MessagingException {
        super.setFlag(flag, set);
        mFolder.setFlags(Collections.singletonList(this), Collections.singleton(flag), set);
    }

    @Override
    public void delete(String trashFolderName) throws MessagingException {
        getFolder().delete(Collections.singletonList(this), trashFolderName);
    }
}
