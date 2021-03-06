package com.whiuk.philip.perfect.helper;


import com.whiuk.philip.perfect.Account;
import com.whiuk.philip.perfect.Identity;
import com.whiuk.philip.perfect.mail.Address;
import com.whiuk.philip.perfect.mail.Message;


public class IdentityHelper {

    /**
     * Find the identity a message was sent to.
     *
     * @param account
     *         The account the message belongs to.
     * @param message
     *         The message to get the recipients from.
     *
     * @return The identity the message was sent to, or the account's default identity if it
     *         couldn't be determined which identity this message was sent to.
     *
     * @see Account#findIdentity(com.whiuk.philip.perfect.mail.Address)
     */
    public static Identity getRecipientIdentityFromMessage(Account account, Message message) {
        Identity recipient = null;

        for (Address address : message.getRecipients(Message.RecipientType.X_ORIGINAL_TO)) {
            Identity identity = account.findIdentity(address);
            if (identity != null) {
                recipient = identity;
                break;
            }
        }

        if (recipient == null) {
            for (Address address : message.getRecipients(Message.RecipientType.DELIVERED_TO)) {
                Identity identity = account.findIdentity(address);
                if (identity != null) {
                    recipient = identity;
                    break;
                }
            }
        }

        if (recipient == null) {
            for (Address address : message.getRecipients(Message.RecipientType.X_ENVELOPE_TO)) {
                Identity identity = account.findIdentity(address);
                if (identity != null) {
                    recipient = identity;
                    break;
                }
            }
        }

        if (recipient == null) {
            for (Address address : message.getRecipients(Message.RecipientType.TO)) {
                Identity identity = account.findIdentity(address);
                if (identity != null) {
                    recipient = identity;
                    break;
                }
            }
        }

        if (recipient == null) {
            Address[] ccAddresses = message.getRecipients(Message.RecipientType.CC);
            if (ccAddresses.length > 0) {
                for (Address address : ccAddresses) {
                    Identity identity = account.findIdentity(address);
                    if (identity != null) {
                        recipient = identity;
                        break;
                    }
                }
            }
        }

        if (recipient == null) {
            recipient = account.getIdentity(0);
        }

        return recipient;
    }
}
