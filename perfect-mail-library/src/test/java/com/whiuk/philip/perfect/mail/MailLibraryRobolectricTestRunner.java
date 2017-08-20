package com.whiuk.philip.perfect.mail;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

public class MailLibraryRobolectricTestRunner extends RobolectricTestRunner {

    public MailLibraryRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Config buildGlobalConfig() {
        return new Config.Builder()
                .setSdk(22)
                .setManifest(Config.NONE)
                .build();
    }
}