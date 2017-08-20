package com.whiuk.philip.perfect;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

public class MailRobolectricTestRunner extends RobolectricTestRunner {

    public MailRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Config buildGlobalConfig() {
        return new Config.Builder()
                .setSdk(22)
                .setManifest("src/main/AndroidManifest.xml")
                .build();
    }
}