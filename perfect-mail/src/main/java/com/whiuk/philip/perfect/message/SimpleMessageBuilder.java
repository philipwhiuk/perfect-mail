package com.whiuk.philip.perfect.message;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.whiuk.philip.perfect.Globals;
import com.whiuk.philip.perfect.mail.BoundaryGenerator;
import com.whiuk.philip.perfect.mail.MessagingException;
import com.whiuk.philip.perfect.mail.internet.MessageIdGenerator;
import com.whiuk.philip.perfect.mail.internet.MimeMessage;


public class SimpleMessageBuilder extends MessageBuilder {

    public static SimpleMessageBuilder newInstance() {
        Context context = Globals.getContext();
        MessageIdGenerator messageIdGenerator = MessageIdGenerator.getInstance();
        BoundaryGenerator boundaryGenerator = BoundaryGenerator.getInstance();
        return new SimpleMessageBuilder(context, messageIdGenerator, boundaryGenerator);
    }

    @VisibleForTesting
    SimpleMessageBuilder(Context context, MessageIdGenerator messageIdGenerator, BoundaryGenerator boundaryGenerator) {
        super(context, messageIdGenerator, boundaryGenerator);
    }

    @Override
    protected void buildMessageInternal() {
        try {
            MimeMessage message = build();
            queueMessageBuildSuccess(message);
        } catch (MessagingException me) {
            queueMessageBuildException(me);
        }
    }

    @Override
    protected void buildMessageOnActivityResult(int requestCode, Intent data) {
        throw new UnsupportedOperationException();
    }
}
