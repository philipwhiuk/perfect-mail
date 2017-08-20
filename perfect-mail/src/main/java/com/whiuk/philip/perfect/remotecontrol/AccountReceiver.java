package com.whiuk.philip.perfect.remotecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import timber.log.Timber;

class AccountReceiver extends BroadcastReceiver {
    MailAccountReceptor receptor = null;

    protected AccountReceiver(MailAccountReceptor nReceptor) {
        receptor = nReceptor;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (RemoteControl.REQUEST_ACCOUNTS.equals(intent.getAction())) {
            Bundle bundle = getResultExtras(false);
            if (bundle == null) {
                Timber.w("Response bundle is empty");
                return;
            }
            receptor.accounts(bundle.getStringArray(RemoteControl.ACCOUNT_UUIDS), bundle.getStringArray(
                    RemoteControl.ACCOUNT_DESCRIPTIONS));
        }
    }

}
