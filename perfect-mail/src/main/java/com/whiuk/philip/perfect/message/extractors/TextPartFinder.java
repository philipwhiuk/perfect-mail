package com.whiuk.philip.perfect.message.extractors;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.whiuk.philip.perfect.mail.Body;
import com.whiuk.philip.perfect.mail.BodyPart;
import com.whiuk.philip.perfect.mail.Multipart;
import com.whiuk.philip.perfect.mail.Part;

import static com.whiuk.philip.perfect.mail.internet.MimeUtility.isSameMimeType;


class TextPartFinder {
    @Nullable
    public Part findFirstTextPart(@NonNull Part part) {
        String mimeType = part.getMimeType();
        Body body = part.getBody();

        if (body instanceof Multipart) {
            Multipart multipart = (Multipart) body;
            if (isSameMimeType(mimeType, "multipart/alternative")) {
                return findTextPartInMultipartAlternative(multipart);
            } else {
                return findTextPartInMultipart(multipart);
            }
        } else if (isSameMimeType(mimeType, "text/plain") || isSameMimeType(mimeType, "text/html")) {
            return part;
        }

        return null;
    }

    private Part findTextPartInMultipartAlternative(Multipart multipart) {
        Part htmlPart = null;

        for (BodyPart bodyPart : multipart.getBodyParts()) {
            String mimeType = bodyPart.getMimeType();
            Body body = bodyPart.getBody();

            if (body instanceof Multipart) {
                Part candidatePart = findFirstTextPart(bodyPart);
                if (candidatePart != null) {
                    if (isSameMimeType(candidatePart.getMimeType(), "text/html")) {
                        htmlPart = candidatePart;
                    } else {
                        return candidatePart;
                    }
                }
            } else if (isSameMimeType(mimeType, "text/plain")) {
                return bodyPart;
            } else if (isSameMimeType(mimeType, "text/html") && htmlPart == null) {
                htmlPart = bodyPart;
            }
        }

        if (htmlPart != null) {
            return htmlPart;
        }

        return null;
    }

    private Part findTextPartInMultipart(Multipart multipart) {
        for (BodyPart bodyPart : multipart.getBodyParts()) {
            String mimeType = bodyPart.getMimeType();
            Body body = bodyPart.getBody();

            if (body instanceof Multipart) {
                Part candidatePart = findFirstTextPart(bodyPart);
                if (candidatePart != null) {
                    return candidatePart;
                }
            } else if (isSameMimeType(mimeType, "text/plain") || isSameMimeType(mimeType, "text/html")) {
                return bodyPart;
            }
        }

        return null;
    }
}
