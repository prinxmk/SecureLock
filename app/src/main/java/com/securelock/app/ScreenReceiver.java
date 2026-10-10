package com.securelock.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Compatibility class for repositories that still contain the older ScreenReceiver.
 * Phase 1.1 monitors screen state from SecureLockService; this receiver remains inert
 * to avoid duplicate lock launches while allowing older manifest/source references to compile.
 */
public final class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Screen monitoring is handled by SecureLockService in Phase 1.1.
    }
}
