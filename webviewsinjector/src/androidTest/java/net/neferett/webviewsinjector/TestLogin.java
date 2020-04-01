/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.neferett.webviewsinjector;

import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.rule.ActivityTestRule;
import org.junit.*;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestLogin {
    @Rule
    public ActivityTestRule<TestConnectionActivity> activityRule = new ActivityTestRule<>(TestConnectionActivity.class);

    @Before
    public void initActivity() {
        activityRule.getActivity();
    }

    @Test
    public void useAppContext() throws InterruptedException {
        Thread.sleep(60000);

        activityRule.getActivity();
    }

}
