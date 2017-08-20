package com.whiuk.philip.perfect.message.extractors;


import java.util.ArrayList;
import java.util.List;

import com.whiuk.philip.perfect.mail.Message;
import com.whiuk.philip.perfect.mail.MessagingException;
import com.whiuk.philip.perfect.mail.Part;
import com.whiuk.philip.perfect.mail.internet.MessageExtractor;


public class AttachmentCounter {
    private final EncryptionDetector encryptionDetector;


    AttachmentCounter(EncryptionDetector encryptionDetector) {
        this.encryptionDetector = encryptionDetector;
    }

    public static AttachmentCounter newInstance() {
        TextPartFinder textPartFinder = new TextPartFinder();
        EncryptionDetector encryptionDetector = new EncryptionDetector(textPartFinder);
        return new AttachmentCounter(encryptionDetector);
    }

    public int getAttachmentCount(Message message) throws MessagingException {
        if (encryptionDetector.isEncrypted(message)) {
            return 0;
        }

        List<Part> attachmentParts = new ArrayList<>();
        MessageExtractor.findViewablesAndAttachments(message, null, attachmentParts);

        return attachmentParts.size();
    }
}
