package com.securelock.app;
import android.accessibilityservice.AccessibilityService;import android.view.accessibility.AccessibilityEvent;
public class SecureAccessibilityService extends AccessibilityService {@Override public void onAccessibilityEvent(AccessibilityEvent e){if(e!=null&&e.getPackageName()!=null){String p=e.getPackageName().toString();if(!p.equals(getPackageName()))SecureLockPrefs.event(this,"Accessibility observed: "+p);}}@Override public void onInterrupt(){}}
