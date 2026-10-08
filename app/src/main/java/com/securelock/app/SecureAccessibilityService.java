package com.securelock.app;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class SecureAccessibilityService extends AccessibilityService {
    private long lastLaunch = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!SecureLockPrefs.isEnabled(this)) return;
        if (event == null || event.getPackageName() == null) return;

        String pkg = event.getPackageName().toString();
        if (pkg.equals(getPackageName())) return;
        if (pkg.equals("com.android.systemui")) return;

        // Phase 1 deliberately uses a conservative policy: the service provides
        // monitoring and a re-entry point. Protected-app selection will be added
        // in the App Lock phase rather than unexpectedly locking every application.
        SecureLockPrefs.setLastPackage(this, pkg);
    }

    @Override
    public void onInterrupt() {
    }
}
