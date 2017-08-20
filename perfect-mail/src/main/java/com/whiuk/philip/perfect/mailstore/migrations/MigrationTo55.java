package com.whiuk.philip.perfect.mailstore.migrations;


import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import timber.log.Timber;

import com.whiuk.philip.perfect.mail.FetchProfile;
import com.whiuk.philip.perfect.mail.MessagingException;
import com.whiuk.philip.perfect.mailstore.LocalFolder;
import com.whiuk.philip.perfect.mailstore.LocalMessage;
import com.whiuk.philip.perfect.mailstore.LocalStore;
import com.whiuk.philip.perfect.message.extractors.MessageFulltextCreator;


class MigrationTo55 {
    static void createFtsSearchTable(SQLiteDatabase db, MigrationsHelper migrationsHelper) {
        db.execSQL("CREATE VIRTUAL TABLE messages_fulltext USING fts4 (fulltext)");

        LocalStore localStore = migrationsHelper.getLocalStore();
        MessageFulltextCreator fulltextCreator = localStore.getMessageFulltextCreator();

        try {
            List<LocalFolder> folders = localStore.getPersonalNamespaces(true);
            ContentValues cv = new ContentValues();
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.BODY);
            for (LocalFolder folder : folders) {
                List<String> messageUids = folder.getAllMessageUids();
                for (String messageUid : messageUids) {
                    LocalMessage localMessage = folder.getMessage(messageUid);
                    folder.fetch(Collections.singletonList(localMessage), fp, null);

                    String fulltext = fulltextCreator.createFulltext(localMessage);
                    if (!TextUtils.isEmpty(fulltext)) {
                        Timber.d("fulltext for msg id %d is %d chars long", localMessage.getId(), fulltext.length());
                        cv.clear();
                        cv.put("docid", localMessage.getId());
                        cv.put("fulltext", fulltext);
                        db.insert("messages_fulltext", null, cv);
                    } else {
                        Timber.d("no fulltext for msg id %d :(", localMessage.getId());
                    }
                }
            }
        } catch (MessagingException e) {
            Timber.e(e, "error indexing fulltext - skipping rest, fts index is incomplete!");
        }
    }
}
