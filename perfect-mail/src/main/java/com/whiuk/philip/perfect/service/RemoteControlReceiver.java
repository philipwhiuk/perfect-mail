
package com.whiuk.philip.perfect.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.whiuk.philip.perfect.remotecontrol.RemoteControl;
import timber.log.Timber;

import com.whiuk.philip.perfect.Account;
import com.whiuk.philip.perfect.Preferences;

import java.util.List;

import static com.whiuk.philip.perfect.remotecontrol.RemoteControl.*;

public class RemoteControlReceiver extends CoreReceiver {
    @Override
    public Integer receive(Context context, Intent intent, Integer tmpWakeLockId) {
        Timber.i("RemoteControlReceiver.onReceive %s", intent);

        if (RemoteControl.K9_SET.equals(intent.getAction())) {
            RemoteControlService.set(context, intent, tmpWakeLockId);
            tmpWakeLockId = null;
        } else if (RemoteControl.REQUEST_ACCOUNTS.equals(intent.getAction())) {
            try {
                Preferences preferences = Preferences.getPreferences(context);
                List<Account> accounts = preferences.getAccounts();
                String[] uuids = new String[accounts.size()];
                String[] descriptions = new String[accounts.size()];
                for (int i = 0; i < accounts.size(); i++) {
                    //warning: account may not be isAvailable()
                    Account account = accounts.get(i);

                    uuids[i] = account.getUuid();
                    descriptions[i] = account.getDescription();
                }
                Bundle bundle = getResultExtras(true);
                bundle.putStringArray(ACCOUNT_UUIDS, uuids);
                bundle.putStringArray(ACCOUNT_DESCRIPTIONS, descriptions);
            } catch (Exception e) {
                Timber.e(e, "Could not handle K9_RESPONSE_INTENT");
            }

        }

        return tmpWakeLockId;
    }

}
