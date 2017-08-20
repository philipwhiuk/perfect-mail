package com.whiuk.philip.perfect.notification;


import com.whiuk.philip.perfect.Account;


public class NotificationGroupKeys {
    private static final String NOTIFICATION_GROUP_KEY_PREFIX = "newMailNotifications-";
    
    
    public static String getGroupKey(Account account) {
        return NOTIFICATION_GROUP_KEY_PREFIX + account.getAccountNumber();
    }
}
