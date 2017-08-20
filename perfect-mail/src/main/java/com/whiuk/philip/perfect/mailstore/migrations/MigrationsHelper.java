package com.whiuk.philip.perfect.mailstore.migrations;


import java.util.List;

import android.content.Context;

import com.whiuk.philip.perfect.Account;
import com.whiuk.philip.perfect.mail.Flag;
import com.whiuk.philip.perfect.mailstore.LocalStore;
import com.whiuk.philip.perfect.preferences.Storage;


/**
 * Helper to allow accessing classes and methods that aren't visible or accessible to the 'migrations' package
 */
public interface MigrationsHelper {
    LocalStore getLocalStore();
    Storage getStorage();
    Account getAccount();
    Context getContext();
    String serializeFlags(List<Flag> flags);
}
