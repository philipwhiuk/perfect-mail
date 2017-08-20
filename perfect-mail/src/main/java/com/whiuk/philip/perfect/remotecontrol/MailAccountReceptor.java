package com.whiuk.philip.perfect.remotecontrol;
/**
 *
 * @author Daniel I. Applebaum
 * The interface to implement in order to accept the arrays containing the UUIDs and descriptions of
 * the accounts configured in Perfect Mail.  Should be passed to fetchAccounts(Context, MailAccountReceptor)
 */
public interface MailAccountReceptor {
    public void accounts(String[] uuids, String[] descriptions);
}