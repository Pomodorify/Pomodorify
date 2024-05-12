package com.example.pomodorify;

import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class TimerEndNotification {
    private final Context context;
    private final NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    public TimerEndNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.builder = null;
    }

    public void buildStandardNotification(String displayText){
        builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.timer)
                .setContentTitle("Session status changed")
                .setContentText(displayText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public void buildSoundLessNotification(String displayText){
        buildStandardNotification(displayText);
        builder.setSilent(true);
    }

    public void sendNotification(){
        notificationManager.notify(1, builder.build());
    }

    public void playSoundOnly(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        ringtone.play();
    }

}
