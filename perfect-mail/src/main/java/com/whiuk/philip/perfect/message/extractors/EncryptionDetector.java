package com.whiuk.philip.perfect.message.extractors;


import android.support.annotation.NonNull;

import com.whiuk.philip.perfect.crypto.MessageDecryptVerifier;
import com.whiuk.philip.perfect.mail.Body;
import com.whiuk.philip.perfect.mail.BodyPart;
import com.whiuk.philip.perfect.mail.Message;
import com.whiuk.philip.perfect.mail.Multipart;
import com.whiuk.philip.perfect.mail.Part;

import static com.whiuk.philip.perfect.mail.internet.MimeUtility.isSameMimeType;


class EncryptionDetector {
    private final TextPartFinder textPartFinder;


    EncryptionDetector(TextPartFinder textPartFinder) {
        this.textPartFinder = textPartFinder;
    }

    public boolean isEncrypted(@NonNull Message message) {
        return isPgpMimeOrSMimeEncrypted(message) || containsInlinePgpEncryptedText(message);
    }

    private boolean isPgpMimeOrSMimeEncrypted(Message message) {
        return containsPartWithMimeType(message, "multipart/encrypted", "application/pkcs7-mime");
    }

    private boolean containsInlinePgpEncryptedText(Message message) {
        Part textPart = textPartFinder.findFirstTextPart(message);
        return MessageDecryptVerifier.isPartPgpInlineEncrypted(textPart);
    }

    private boolean containsPartWithMimeType(Part part, String... wantedMimeTypes) {
        String mimeType = part.getMimeType();
        if (isMimeTypeAnyOf(mimeType, wantedMimeTypes)) {
            return true;
        }

        Body body = part.getBody();
        if (body instanceof Multipart) {
            Multipart multipart = (Multipart) body;
            for (BodyPart bodyPart : multipart.getBodyParts()) {
                if (containsPartWithMimeType(bodyPart, wantedMimeTypes)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isMimeTypeAnyOf(String mimeType, String... wantedMimeTypes) {
        for (String wantedMimeType : wantedMimeTypes) {
            if (isSameMimeType(mimeType, wantedMimeType)) {
                return true;
            }
        }

        return false;
    }
}
