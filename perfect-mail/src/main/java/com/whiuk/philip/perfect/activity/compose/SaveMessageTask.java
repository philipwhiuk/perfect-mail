package com.whiuk.philip.perfect.activity.compose;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.whiuk.philip.perfect.Account;
import com.whiuk.philip.perfect.activity.MessageCompose;
import com.whiuk.philip.perfect.controller.MessagingController;
import com.whiuk.philip.perfect.helper.Contacts;
import com.whiuk.philip.perfect.mail.Message;

public class SaveMessageTask extends AsyncTask<Void, Void, Void> {
    Context context;
    Account account;
    Contacts contacts;
    Handler handler;
    Message message;
    long draftId;
    boolean saveRemotely;

    public SaveMessageTask(Context context, Account account, Contacts contacts,
                           Handler handler, Message message, long draftId, boolean saveRemotely) {
        this.context = context;
        this.account = account;
        this.contacts = contacts;
        this.handler = handler;
        this.message = message;
        this.draftId = draftId;
        this.saveRemotely = saveRemotely;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final MessagingController messagingController = MessagingController.getInstance(context);
        Message draftMessage = messagingController.saveDraft(account, message, draftId, saveRemotely);
        draftId = messagingController.getId(draftMessage);

        android.os.Message msg = android.os.Message.obtain(handler, MessageCompose.MSG_SAVED_DRAFT, draftId);
        handler.sendMessage(msg);
        return null;
    }
}
