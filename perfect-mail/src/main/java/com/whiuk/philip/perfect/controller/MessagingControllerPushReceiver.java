package com.whiuk.philip.perfect.controller;

import android.content.Context;

import com.whiuk.philip.perfect.PerfectMail;
import timber.log.Timber;

import com.whiuk.philip.perfect.Account;
import com.whiuk.philip.perfect.mail.power.TracingPowerManager.TracingWakeLock;
import com.whiuk.philip.perfect.mail.Folder;

import com.whiuk.philip.perfect.mail.Message;
import com.whiuk.philip.perfect.mail.PushReceiver;
import com.whiuk.philip.perfect.mailstore.LocalFolder;
import com.whiuk.philip.perfect.mailstore.LocalStore;
import com.whiuk.philip.perfect.service.SleepService;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MessagingControllerPushReceiver implements PushReceiver {
    final Account account;
    final MessagingController controller;
    final Context context;

    public MessagingControllerPushReceiver(Context context, Account nAccount, MessagingController nController) {
        account = nAccount;
        controller = nController;
        this.context = context;
    }

    public void messagesFlagsChanged(Folder folder,
                                     List<Message> messages) {
        controller.messagesArrived(account, folder, messages, true);
    }
    public void messagesArrived(Folder folder, List<Message> messages) {
        controller.messagesArrived(account, folder, messages, false);
    }
    public void messagesRemoved(Folder folder, List<Message> messages) {
        controller.messagesArrived(account, folder, messages, true);
    }

    public void syncFolder(Folder folder) {
        Timber.v("syncFolder(%s)", folder.getName());

        final CountDownLatch latch = new CountDownLatch(1);
        controller.synchronizeMailbox(account, folder.getName(), new SimpleMessagingListener() {
            @Override
            public void synchronizeMailboxFinished(Account account, String folder,
            int totalMessagesInMailbox, int numNewMessages) {
                latch.countDown();
            }

            @Override
            public void synchronizeMailboxFailed(Account account, String folder,
            String message) {
                latch.countDown();
            }
        }, folder);

        Timber.v("syncFolder(%s) about to await latch release", folder.getName());

        try {
            latch.await();
            Timber.v("syncFolder(%s) got latch release", folder.getName());
        } catch (Exception e) {
            Timber.e(e, "Interrupted while awaiting latch release");
        }
    }

    @Override
    public void sleep(TracingWakeLock wakeLock, long millis) {
        SleepService.sleep(context, millis, wakeLock, PerfectMail.PUSH_WAKE_LOCK_TIMEOUT);
    }

    public void pushError(String errorMessage, Exception e) {
        String errMess = errorMessage;

        controller.notifyUserIfCertificateProblem(account, e, true);
        if (errMess == null && e != null) {
            errMess = e.getMessage();
        }
        controller.addErrorMessage(account, errMess, e);
    }

    @Override
    public void authenticationFailed() {
        controller.handleAuthenticationFailure(account, true);
    }

    public String getPushState(String folderName) {
        LocalFolder localFolder = null;
        try {
            LocalStore localStore = account.getLocalStore();
            localFolder = localStore.getFolder(folderName);
            localFolder.open(Folder.OPEN_MODE_RW);
            return localFolder.getPushState();
        } catch (Exception e) {
            Timber.e(e, "Unable to get push state from account %s, folder %s", account.getDescription(), folderName);
            return null;
        } finally {
            if (localFolder != null) {
                localFolder.close();
            }
        }
    }

    public void setPushActive(String folderName, boolean enabled) {
        for (MessagingListener l : controller.getListeners()) {
            l.setPushActive(account, folderName, enabled);
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

}
