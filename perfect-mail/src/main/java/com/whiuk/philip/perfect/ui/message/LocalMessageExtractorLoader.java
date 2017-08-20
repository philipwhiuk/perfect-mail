package com.whiuk.philip.perfect.ui.message;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import timber.log.Timber;

import com.whiuk.philip.perfect.mail.Message;
import com.whiuk.philip.perfect.mailstore.LocalMessage;
import com.whiuk.philip.perfect.mailstore.MessageViewInfoExtractor;
import com.whiuk.philip.perfect.mailstore.MessageViewInfo;
import com.whiuk.philip.perfect.ui.crypto.MessageCryptoAnnotations;


public class LocalMessageExtractorLoader extends AsyncTaskLoader<MessageViewInfo> {
    private static final MessageViewInfoExtractor messageViewInfoExtractor = MessageViewInfoExtractor.getInstance();


    private final Message message;
    private MessageViewInfo messageViewInfo;
    @Nullable
    private MessageCryptoAnnotations annotations;

    public LocalMessageExtractorLoader(
            Context context, Message message, @Nullable MessageCryptoAnnotations annotations) {
        super(context);
        this.message = message;
        this.annotations = annotations;
    }

    @Override
    protected void onStartLoading() {
        if (messageViewInfo != null) {
            super.deliverResult(messageViewInfo);
        }

        if (takeContentChanged() || messageViewInfo == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(MessageViewInfo messageViewInfo) {
        this.messageViewInfo = messageViewInfo;
        super.deliverResult(messageViewInfo);
    }

    @Override
    @WorkerThread
    public MessageViewInfo loadInBackground() {
        try {
            return messageViewInfoExtractor.extractMessageForView(message, annotations);
        } catch (Exception e) {
            Timber.e(e, "Error while decoding message");
            return null;
        }
    }

    public boolean isCreatedFor(LocalMessage localMessage, MessageCryptoAnnotations messageCryptoAnnotations) {
        return annotations == messageCryptoAnnotations && message.equals(localMessage);
    }
}
