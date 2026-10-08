package com.securelock.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private LinearLayout root;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi();
        requestNotificationPermission();
        startMonitor();
    }

    private void buildUi() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 32, 32, 32);
        root.setBackgroundColor(Color.rgb(11, 18, 32));

        TextView title = new TextView(this);
        title.setText("SecureLock");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        root.addView(title, params());

        TextView subtitle = new TextView(this);
        subtitle.setText("Independent application-level security layer");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle, params());

        status = new TextView(this);
        status.setTextColor(Color.WHITE);
        status.setTextSize(17);
        status.setPadding(0, 28, 0, 28);
        root.addView(status, params());

        Button setup = button("Set / Change PIN");
        setup.setOnClickListener(v -> setPin());
        root.addView(setup, params());

        Button test = button("Test SecureLock");
        test.setOnClickListener(v -> LockActivity.requestLaunch(this, "manual_test"));
        root.addView(test, params());

        Button accessibility = button("Enable Accessibility Protection");
        accessibility.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        });
        root.addView(accessibility, params());

        Button overlay = button("Allow Display Over Other Apps");
        overlay.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
            }
        });
        root.addView(overlay, params());

        TextView warning = new TextView(this);
        warning.setText(
            "Important: SecureLock does NOT replace Android's underlying system " +
            "credential protection. It is an additional application-level security " +
            "layer. Android may restrict automatic activity launches on some devices."
        );
        warning.setTextColor(Color.LTGRAY);
        warning.setPadding(0, 28, 0, 0);
        root.addView(warning, params());

        setContentView(root);
        refreshStatus();
    }

    private void setPin() {
        final EditText input = new EditText(this);
        input.setHint("Enter 6–12 digit PIN");
        input.setInputType(2);
        new AlertDialogBuilder(this)
            .title("SecureLock PIN")
            .view(input)
            .positive("Save", () -> {
                String pin = input.getText().toString();
                if (pin.matches("\\d{6,12}")) {
                    try {
                        SecureLockPrefs.savePin(this, pin);
                        Toast.makeText(this, "PIN saved and SecureLock enabled.", Toast.LENGTH_SHORT).show();
                        refreshStatus();
                    } catch (Exception e) {
                        Toast.makeText(this, "Could not save PIN.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Use a PIN of 6–12 digits.", Toast.LENGTH_SHORT).show();
                }
            })
            .negative("Cancel", null)
            .show();
    }

    private void refreshStatus() {
        if (SecureLockPrefs.hasPin(this)) {
            status.setText("● SecureLock is configured\n● PIN protection is enabled\n● Boot monitoring: enabled");
        } else {
            status.setText("○ SecureLock is not configured\nSet a 6–12 digit PIN to activate protection.");
        }
    }

    private void startMonitor() {
        Intent service = new Intent(this, SecureLockService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(service);
        else startService(service);
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 55);
        }
    }

    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setAllCaps(false);
        return b;
    }

    private LinearLayout.LayoutParams params() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    private static class AlertDialogBuilder {
        private final android.app.AlertDialog.Builder builder;
        AlertDialogBuilder(Activity a) { builder = new android.app.AlertDialog.Builder(a); }
        AlertDialogBuilder title(String s) { builder.setTitle(s); return this; }
        AlertDialogBuilder view(View v) { builder.setView(v); return this; }
        AlertDialogBuilder positive(String s, Runnable r) {
            builder.setPositiveButton(s, (d,w) -> r.run()); return this;
        }
        AlertDialogBuilder negative(String s, Object ignored) {
            builder.setNegativeButton(s, null); return this;
        }
        void show() { builder.show(); }
    }
}
