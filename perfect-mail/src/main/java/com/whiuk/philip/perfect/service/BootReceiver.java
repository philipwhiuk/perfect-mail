
package com.whiuk.philip.perfect.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.whiuk.philip.perfect.PerfectMail;
import timber.log.Timber;

import com.whiuk.philip.perfect.helper.MailAlarmManager;

public class BootReceiver extends CoreReceiver {

    public static final String FIRE_INTENT = "com.whiuk.philip.perfect.service.BroadcastReceiver.fireIntent";
    public static final String SCHEDULE_INTENT = "com.whiuk.philip.perfect.service.BroadcastReceiver.scheduleIntent";
    public static final String CANCEL_INTENT = "com.whiuk.philip.perfect.service.BroadcastReceiver.cancelIntent";

    public static final String ALARMED_INTENT = "com.whiuk.philip.perfect.service.BroadcastReceiver.pendingIntent";
    public static final String AT_TIME = "com.whiuk.philip.perfect.service.BroadcastReceiver.atTime";

    @Override
    public Integer receive(Context context, Intent intent, Integer tmpWakeLockId) {
        Timber.i("BootReceiver.onReceive %s", intent);

        final String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            //PerfectMail.setServicesEnabled(context, tmpWakeLockId);
            //tmpWakeLockId = null;
        } else if (Intent.ACTION_DEVICE_STORAGE_LOW.equals(action)) {
            MailService.actionCancel(context, tmpWakeLockId);
            tmpWakeLockId = null;
        } else if (Intent.ACTION_DEVICE_STORAGE_OK.equals(action)) {
            MailService.actionReset(context, tmpWakeLockId);
            tmpWakeLockId = null;
        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            MailService.connectivityChange(context, tmpWakeLockId);
            tmpWakeLockId = null;
        } else if ("com.android.sync.SYNC_CONN_STATUS_CHANGED".equals(action)) {
            PerfectMail.BACKGROUND_OPS bOps = PerfectMail.getBackgroundOps();
            if (bOps == PerfectMail.BACKGROUND_OPS.WHEN_CHECKED_AUTO_SYNC) {
                MailService.actionReset(context, tmpWakeLockId);
                tmpWakeLockId = null;
            }
        } else if (FIRE_INTENT.equals(action)) {
            Intent alarmedIntent = intent.getParcelableExtra(ALARMED_INTENT);
            String alarmedAction = alarmedIntent.getAction();
            Timber.i("BootReceiver Got alarm to fire alarmedIntent %s", alarmedAction);
            alarmedIntent.putExtra(WAKE_LOCK_ID, tmpWakeLockId);
            tmpWakeLockId = null;
            context.startService(alarmedIntent);
        } else if (SCHEDULE_INTENT.equals(action)) {
            long atTime = intent.getLongExtra(AT_TIME, -1);
            Intent alarmedIntent = intent.getParcelableExtra(ALARMED_INTENT);
            Timber.i("BootReceiver Scheduling intent %s for %tc", alarmedIntent, atTime);

            PendingIntent pi = buildPendingIntent(context, intent);
            MailAlarmManager alarmMgr = MailAlarmManager.getAlarmManager(context);

            alarmMgr.set(AlarmManager.RTC_WAKEUP, atTime, pi);
        } else if (CANCEL_INTENT.equals(action)) {
            Intent alarmedIntent = intent.getParcelableExtra(ALARMED_INTENT);
            Timber.i("BootReceiver Canceling alarmedIntent %s", alarmedIntent);

            PendingIntent pi = buildPendingIntent(context, intent);

            MailAlarmManager alarmMgr = MailAlarmManager.getAlarmManager(context);
            alarmMgr.cancel(pi);
        }


        return tmpWakeLockId;
    }

    private PendingIntent buildPendingIntent(Context context, Intent intent) {
        Intent alarmedIntent = intent.getParcelableExtra(ALARMED_INTENT);
        String alarmedAction = alarmedIntent.getAction();

        Intent i = new Intent(context, BootReceiver.class);
        i.setAction(FIRE_INTENT);
        i.putExtra(ALARMED_INTENT, alarmedIntent);
        Uri uri = Uri.parse("action://" + alarmedAction);
        i.setData(uri);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        return pi;
    }

    public static void scheduleIntent(Context context, long atTime, Intent alarmedIntent) {
        Timber.i("BootReceiver Got request to schedule alarmedIntent %s", alarmedIntent.getAction());

        Intent i = new Intent();
        i.setClass(context, BootReceiver.class);
        i.setAction(SCHEDULE_INTENT);
        i.putExtra(ALARMED_INTENT, alarmedIntent);
        i.putExtra(AT_TIME, atTime);
        context.sendBroadcast(i);
    }

    public static void cancelIntent(Context context, Intent alarmedIntent) {
        Timber.i("BootReceiver Got request to cancel alarmedIntent %s", alarmedIntent.getAction());

        Intent i = new Intent();
        i.setClass(context, BootReceiver.class);
        i.setAction(CANCEL_INTENT);
        i.putExtra(ALARMED_INTENT, alarmedIntent);
        context.sendBroadcast(i);
    }

    /**
     * Cancel any scheduled alarm.
     *
     * @param context
     */
    public static void purgeSchedule(final Context context) {
        final MailAlarmManager alarmService = MailAlarmManager.getAlarmManager(context);
        alarmService.cancel(PendingIntent.getBroadcast(context, 0, new Intent() {
            @Override
            public boolean filterEquals(final Intent other) {
                // we want to match all intents
                return true;
            }
        }, 0));
    }

}
