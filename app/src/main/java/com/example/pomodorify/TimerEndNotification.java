package com.example.pomodorify;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;


public class TimerEndNotification {
    private Context context;
    private NotificationManager notificationManager;

    public TimerEndNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.timer)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }


}
