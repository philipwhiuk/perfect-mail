package com.whiuk.philip.perfect.power;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;

import com.whiuk.philip.perfect.service.MailService;
import timber.log.Timber;


@RequiresApi(api = Build.VERSION_CODES.M)
class DeviceIdleReceiver extends BroadcastReceiver {
    private final PowerManager powerManager;


    DeviceIdleReceiver(PowerManager powerManager) {
        this.powerManager = powerManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean deviceInIdleMode = powerManager.isDeviceIdleMode();
        Timber.v("Device idle mode changed. Idle: %b", deviceInIdleMode);

        if (!deviceInIdleMode) {
            MailService.actionReset(context, null);
        }
    }
}
