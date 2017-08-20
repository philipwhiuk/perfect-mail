package com.whiuk.philip.perfect.remotecontrol;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.whiuk.philip.perfect.BuildConfig;

/**
 * Utility definitions for Android applications to control the behavior of K-9 Mail.  All such applications must declare the following permission:
 * <uses-permission android:name="com.whiuk.philip.k9.permission.REMOTE_CONTROL"/>
 * in their AndroidManifest.xml  In addition, all applications sending remote control messages to K-9 Mail must
 *
 * An application that wishes to act on a particular Account in K-9 needs to fetch the list of configured Accounts by broadcasting an
 * {@link Intent} using REQUEST_ACCOUNTS as the Action.  The broadcast must be made using the {@link android.content.ContextWrapper}
 * sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver,
 * Handler scheduler, int initialCode, String initialData, Bundle initialExtras).sendOrderedBroadcast}
 * method in order to receive the list of Account UUIDs and descriptions that K-9 will provide.
 *
 * @author Daniel I. Applebaum
 *
 */
public class RemoteControl {

    /**
     * Permission that every application sending a broadcast to K-9 for Remote Control purposes should send on every broadcast.
     * Prevent other applications from intercepting the broadcasts.
     */
    public final static String REMOTE_CONTROL_PERMISSION = BuildConfig.APPLICATION_ID + ".permission.REMOTE_CONTROL";
    /**
     * {@link Intent} Action to be sent to K-9 using {@link android.content.ContextWrapper#sendOrderedBroadcast} in order to fetch the list of configured Accounts.
     * The responseData will contain two String[] with keys ACCOUNT_UUIDS and ACCOUNT_DESCRIPTIONS
     */
    public final static String REQUEST_ACCOUNTS = BuildConfig.APPLICATION_ID + ".RemoteControl.requestAccounts";
    public final static String ACCOUNT_UUIDS = BuildConfig.APPLICATION_ID + ".RemoteControl.accountUuids";
    public final static String ACCOUNT_DESCRIPTIONS = BuildConfig.APPLICATION_ID + ".RemoteControl.accountDescriptions";

    /**
     * The {@link {@link Intent}} Action to set in order to cause Perfect Mail to check mail.  (Not yet implemented)
     */
    public final static String CHECK_MAIL = "com.whiuk.philip.perfect.RemoteControl.checkMail";

    /**
     * The {@link {@link Intent}} Action to set when remotely changing K-9 Mail settings
     */
    public final static String SET = BuildConfig.APPLICATION_ID + ".RemoteControl.set";
    /**
     * The key of the {@link Intent} Extra to set to hold the UUID of a single Account's settings to change.  Used only if K9_ALL_ACCOUNTS
     * is absent or false.
     */
    public final static String ACCOUNT_UUID = BuildConfig.APPLICATION_ID + ".RemoteControl.accountUuid";
    /**
     * The key of the {@link Intent} Extra to set to control if the settings will apply to all Accounts, or to the one
     * specified with K9_ACCOUNT_UUID
     */
    public final static String ALL_ACCOUNTS = BuildConfig.APPLICATION_ID + ".RemoteControl.allAccounts";

    public final static String ENABLED = "true";
    public final static String DISABLED = "false";

    /*
     * Key for the {@link Intent} Extra for controlling whether notifications will be generated for new unread mail.
     * Acceptable values are K9_ENABLED and K9_DISABLED
     */
    public final static String K9_NOTIFICATION_ENABLED = BuildConfig.APPLICATION_ID + ".RemoteControl.notificationEnabled";
    /*
     * Key for the {@link Intent} Extra for controlling whether K-9 will sound the ringtone for new unread mail.
     * Acceptable values are K9_ENABLED and K9_DISABLED
     */
    public final static String K9_RING_ENABLED = BuildConfig.APPLICATION_ID + ".RemoteControl.ringEnabled";
    /*
     * Key for the {@link Intent} Extra for controlling whether K-9 will activate the vibrator for new unread mail.
     * Acceptable values are K9_ENABLED and K9_DISABLED
     */
    public final static String K9_VIBRATE_ENABLED = BuildConfig.APPLICATION_ID + ".RemoteControl.vibrateEnabled";

    public final static String K9_FOLDERS_NONE = "NONE";
    public final static String K9_FOLDERS_ALL = "ALL";
    public final static String K9_FOLDERS_FIRST_CLASS = "FIRST_CLASS";
    public final static String K9_FOLDERS_FIRST_AND_SECOND_CLASS = "FIRST_AND_SECOND_CLASS";
    public final static String K9_FOLDERS_NOT_SECOND_CLASS = "NOT_SECOND_CLASS";
    /**
     * Key for the {@link Intent} Extra to set for controlling which folders to be synchronized with Push.
     * Acceptable values are K9_FOLDERS_ALL, K9_FOLDERS_FIRST_CLASS, K9_FOLDERS_FIRST_AND_SECOND_CLASS,
     * K9_FOLDERS_NOT_SECOND_CLASS, K9_FOLDERS_NONE
     */
    public final static String K9_PUSH_CLASSES = BuildConfig.APPLICATION_ID + ".RemoteControl.pushClasses";
    /**
     * Key for the {@link Intent} Extra to set for controlling which folders to be synchronized with Poll.
     * Acceptable values are K9_FOLDERS_ALL, K9_FOLDERS_FIRST_CLASS, K9_FOLDERS_FIRST_AND_SECOND_CLASS,
     * K9_FOLDERS_NOT_SECOND_CLASS, K9_FOLDERS_NONE
     */
    public final static String K9_POLL_CLASSES = BuildConfig.APPLICATION_ID + ".RemoteControl.pollClasses";

    public final static String[] K9_POLL_FREQUENCIES = { "-1", "1", "5", "10", "15", "30", "60", "120", "180", "360", "720", "1440"};
    /**
     * Key for the {@link Intent} Extra to set with the desired poll frequency.  The value is a String representing a number of minutes.
     * Acceptable values are available in K9_POLL_FREQUENCIES
     */
    public final static String K9_POLL_FREQUENCY = BuildConfig.APPLICATION_ID + ".RemoteControl.pollFrequency";

    /**
     * Key for the {@link Intent} Extra to set for controlling K-9's global "Background sync" setting.
     * Acceptable values are K9_BACKGROUND_OPERATIONS_ALWAYS, K9_BACKGROUND_OPERATIONS_NEVER
     * K9_BACKGROUND_OPERATIONS_WHEN_CHECKED_AUTO_SYNC
     */
    public final static String K9_BACKGROUND_OPERATIONS = BuildConfig.APPLICATION_ID + ".RemoteControl.backgroundOperations";
    public final static String K9_BACKGROUND_OPERATIONS_ALWAYS = "ALWAYS";
    public final static String K9_BACKGROUND_OPERATIONS_NEVER = "NEVER";
    public final static String K9_BACKGROUND_OPERATIONS_WHEN_CHECKED_AUTO_SYNC = "WHEN_CHECKED_AUTO_SYNC";

    /**
     * Key for the {@link Intent} Extra to set for controlling which display theme K-9 will use.  Acceptable values are
     * K9_THEME_LIGHT, K9_THEME_DARK
     */
    public final static String K9_THEME = BuildConfig.APPLICATION_ID + ".RemoteControl.theme";
    public final static String K9_THEME_LIGHT = "LIGHT";
    public final static String K9_THEME_DARK = "DARK";

    protected static final String LOG_TAG = "RemoteControl";

    public static void set(Context context, Intent broadcastIntent) {
        broadcastIntent.setAction(RemoteControl.SET);
        context.sendBroadcast(broadcastIntent, RemoteControl.REMOTE_CONTROL_PERMISSION);
    }

    public static void fetchAccounts(Context context, MailAccountReceptor receptor) {
        Intent accountFetchIntent = new Intent();
        accountFetchIntent.setAction(RemoteControl.REQUEST_ACCOUNTS);
        AccountReceiver receiver = new AccountReceiver(receptor);
        context.sendOrderedBroadcast(accountFetchIntent, RemoteControl.REMOTE_CONTROL_PERMISSION, receiver, null, Activity.RESULT_OK, null, null);
    }

}


