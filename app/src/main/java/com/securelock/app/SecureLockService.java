package com.securelock.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class SecureLockService extends Service {
    private static final String CHANNEL_ID = "securelock_security";
    private static final int NOTIFICATION_ID = 4101;

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("SecureLock is active")
            .setContentText("Security monitoring is running.")
            .setSmallIcon(com.securelock.app.R.drawable.ic_securelock)
            .setOngoing(true)
            .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "SecureLock Security", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Persistent SecureLock security status");
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
