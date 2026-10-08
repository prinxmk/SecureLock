package com.securelock.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

public class LockActivity extends Activity {
    private EditText pinInput;
    private Button unlock;
    private TextView message;
    private long blockedUntil = 0;

    public static void requestLaunch(Context context, String reason) {
        Intent i = new Intent(context, LockActivity.class);
        i.putExtra("reason", reason);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                   Intent.FLAG_ACTIVITY_CLEAR_TOP |
                   Intent.FLAG_ACTIVITY_SINGLE_TOP);
        try {
            context.startActivity(i);
        } catch (Exception ignored) {
            // Android may prevent background activity launch. Accessibility-based
            // enforcement is the stronger mechanism on supported devices.
        }
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                   WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                   WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        build();
    }

    private void build() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(45, 45, 45, 45);
        box.setBackgroundColor(Color.rgb(7, 13, 24));

        TextView logo = new TextView(this);
        logo.setText("🔒");
        logo.setTextSize(52);
        logo.setGravity(Gravity.CENTER);
        box.addView(logo);

        TextView title = new TextView(this);
        title.setText("SecureLock");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        box.addView(title);

        TextView sub = new TextView(this);
        sub.setText("Enter your SecureLock PIN");
        sub.setTextColor(Color.LTGRAY);
        sub.setGravity(Gravity.CENTER);
        box.addView(sub);

        pinInput = new EditText(this);
        pinInput.setInputType(2);
        pinInput.setHint("PIN");
        pinInput.setTextColor(Color.WHITE);
        pinInput.setHintTextColor(Color.GRAY);
        pinInput.setGravity(Gravity.CENTER);
        box.addView(pinInput, new LinearLayout.LayoutParams(-1, -2));

        unlock = new Button(this);
        unlock.setText("Unlock SecureLock");
        unlock.setAllCaps(false);
        unlock.setOnClickListener(v -> verify());
        box.addView(unlock, new LinearLayout.LayoutParams(-1, -2));

        message = new TextView(this);
        message.setTextColor(Color.LTGRAY);
        message.setGravity(Gravity.CENTER);
        box.addView(message);

        setContentView(box);
    }

    private void verify() {
        if (System.currentTimeMillis() < blockedUntil) {
            message.setText("Too many attempts. Please wait.");
            return;
        }

        String pin = pinInput.getText().toString();
        if (SecureLockPrefs.verifyPin(this, pin)) {
            SecureLockPrefs.resetFailures(this);
            finishAndRemoveTask();
        } else {
            int failures = SecureLockPrefs.registerFailure(this);
            pinInput.setText("");

            if (failures >= 5) {
                blockedUntil = System.currentTimeMillis() + 30000;
                message.setText("Too many failed attempts. Locked for 30 seconds.");
                new CountDownTimer(30000, 1000) {
                    public void onTick(long ms) {
                        message.setText("Try again in " + ((ms + 999) / 1000) + " seconds.");
                    }
                    public void onFinish() {
                        message.setText("You may try again.");
                    }
                }.start();
            } else {
                message.setText("Incorrect PIN. Attempt " + failures + ".");
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Do not allow the SecureLock screen to be dismissed by Back.
    }
}
