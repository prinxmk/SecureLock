package com.securelock.app;

import android.content.Context;
import android.content.SharedPreferences;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class SecureLockPrefs {
    private static final String PREFS = "secure_lock";
    private static final String KEY_HASH = "pin_hash";
    private static final String KEY_SALT = "pin_salt";
    private static final String KEY_ENABLED = "lock_enabled";
    private static final String KEY_BOOT = "boot_enabled";
    private static final String KEY_LOCK_SCREEN = "lock_screen_enabled";
    private static final String KEY_FAILED = "failed_attempts";
    private static final String KEY_LAST_PACKAGE = "last_package";

    private SecureLockPrefs() {}

    private static SharedPreferences p(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static boolean hasPin(Context c) {
        return p(c).contains(KEY_HASH) && p(c).contains(KEY_SALT);
    }

    public static boolean isEnabled(Context c) {
        return p(c).getBoolean(KEY_ENABLED, false);
    }

    public static void setEnabled(Context c, boolean value) {
        p(c).edit().putBoolean(KEY_ENABLED, value).apply();
    }

    public static boolean bootEnabled(Context c) {
        return p(c).getBoolean(KEY_BOOT, true);
    }

    public static boolean lockScreenEnabled(Context c) {
        return p(c).getBoolean(KEY_LOCK_SCREEN, true);
    }

    public static void setLockScreenEnabled(Context c, boolean value) {
        p(c).edit().putBoolean(KEY_LOCK_SCREEN, value).apply();
    }

    public static void savePin(Context c, String pin) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] hash = hash(pin, salt);
        p(c).edit()
            .putString(KEY_SALT, hex(salt))
            .putString(KEY_HASH, hex(hash))
            .putBoolean(KEY_ENABLED, true)
            .apply();
    }

    public static boolean verifyPin(Context c, String pin) {
        try {
            String saltHex = p(c).getString(KEY_SALT, "");
            String hashHex = p(c).getString(KEY_HASH, "");
            if (saltHex.isEmpty() || hashHex.isEmpty()) return false;
            byte[] expected = hash(pin, fromHex(saltHex));
            return MessageDigest.isEqual(expected, fromHex(hashHex));
        } catch (Exception e) {
            return false;
        }
    }

    public static int failed(Context c) {
        return p(c).getInt(KEY_FAILED, 0);
    }

    public static int registerFailure(Context c) {
        int n = failed(c) + 1;
        p(c).edit().putInt(KEY_FAILED, n).apply();
        return n;
    }

    public static void resetFailures(Context c) {
        p(c).edit().putInt(KEY_FAILED, 0).apply();
    }

    public static void setLastPackage(Context c, String packageName) {
        p(c).edit().putString(KEY_LAST_PACKAGE, packageName).apply();
    }

    public static String lastPackage(Context c) {
        return p(c).getString(KEY_LAST_PACKAGE, "");
    }

    private static byte[] hash(String value, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        return md.digest(value.getBytes("UTF-8"));
    }

    private static String hex(byte[] b) {
        StringBuilder s = new StringBuilder();
        for (byte x : b) s.append(String.format("%02x", x & 0xff));
        return s.toString();
    }

    private static byte[] fromHex(String s) {
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }
}
