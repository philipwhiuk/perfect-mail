package com.whiuk.philip.perfect.mail.internet;


import com.whiuk.philip.perfect.mail.Body;


/**
 * See {@link MimeUtility#decodeBody(Body)}
 */
public interface RawDataBody extends Body {
    String getEncoding();
}
