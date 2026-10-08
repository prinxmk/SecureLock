package com.securelock.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            if (SecureLockPrefs.isEnabled(context) && SecureLockPrefs.lockScreenEnabled(context)) {
                LockActivity.requestLaunch(context, "screen_off");
            }
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            // The system has authenticated the user. SecureLock remains available
            // as an application-level protection layer; it does not replace the
            // Android credential screen.
        }
    }
}
