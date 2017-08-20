package com.whiuk.philip.perfect.mail.store.imap;


import java.util.ArrayList;
import java.util.List;

import com.whiuk.philip.perfect.mail.MailLibrary;
import com.whiuk.philip.perfect.mail.PushReceiver;
import com.whiuk.philip.perfect.mail.Pusher;
import timber.log.Timber;


class ImapPusher implements Pusher {
    private final ImapStore store;
    private final PushReceiver pushReceiver;

    private final List<ImapFolderPusher> folderPushers = new ArrayList<>();

    private long lastRefresh = -1;


    public ImapPusher(ImapStore store, PushReceiver pushReceiver) {
        this.store = store;
        this.pushReceiver = pushReceiver;
    }

    @Override
    public void start(List<String> folderNames) {
        synchronized (folderPushers) {
            stop();

            setLastRefresh(currentTimeMillis());

            for (String folderName : folderNames) {
                ImapFolderPusher pusher = createImapFolderPusher(folderName);
                folderPushers.add(pusher);

                pusher.start();
            }
        }
    }

    @Override
    public void refresh() {
        synchronized (folderPushers) {
            for (ImapFolderPusher folderPusher : folderPushers) {
                try {
                    folderPusher.refresh();
                } catch (Exception e) {
                    Timber.e(e, "Got exception while refreshing for %s", folderPusher.getName());
                }
            }
        }
    }

    @Override
    public void stop() {
        if (MailLibrary.isDebug()) {
            Timber.i("Requested stop of IMAP pusher");
        }

        synchronized (folderPushers) {
            for (ImapFolderPusher folderPusher : folderPushers) {
                try {
                    if (MailLibrary.isDebug()) {
                        Timber.i("Requesting stop of IMAP folderPusher %s", folderPusher.getName());
                    }

                    folderPusher.stop();
                } catch (Exception e) {
                    Timber.e(e, "Got exception while stopping %s", folderPusher.getName());
                }
            }

            folderPushers.clear();
        }
    }

    @Override
    public int getRefreshInterval() {
        return (store.getStoreConfig().getIdleRefreshMinutes() * 60 * 1000);
    }

    @Override
    public long getLastRefresh() {
        return lastRefresh;
    }

    @Override
    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    ImapFolderPusher createImapFolderPusher(String folderName) {
        return new ImapFolderPusher(store, folderName, pushReceiver);
    }

    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
