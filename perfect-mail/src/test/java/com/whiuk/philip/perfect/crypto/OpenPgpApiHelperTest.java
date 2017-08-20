package com.whiuk.philip.perfect.crypto;


import com.whiuk.philip.perfect.Identity;
import com.whiuk.philip.perfect.MailRobolectricTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(MailRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class OpenPgpApiHelperTest {

    @Test
    public void buildUserId_withName_shouldCreateOpenPgpAccountName() {
        Identity identity = new Identity();
        identity.setEmail("user@domain.com");
        identity.setName("Name");

        String result = OpenPgpApiHelper.buildUserId(identity);

        assertEquals("Name <user@domain.com>", result);
    }

    @Test
    public void buildUserId_withoutName_shouldCreateOpenPgpAccountName() {
        Identity identity = new Identity();
        identity.setEmail("user@domain.com");

        String result = OpenPgpApiHelper.buildUserId(identity);

        assertEquals("<user@domain.com>", result);
    }

}
