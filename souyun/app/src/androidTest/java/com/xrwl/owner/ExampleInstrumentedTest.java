package com.xrwl.owner;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(ExampleInstrumentedTest.AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private InstrumentationTestCase InstrumentationRegistry;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
     
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.xrwl.owner", appContext.getPackageName());
    }

    class AndroidJUnit4 extends Runner {
        @Override
        public Description getDescription() {
            return null;
        }

        @Override
        public void run(RunNotifier notifier) {

        }
    }
}
