package com.whiuk.philip.perfect.mailstore;


public interface LocalPart {
    String getAccountUuid();
    long getId();
    long getSize();
    LocalMessage getMessage();
}
