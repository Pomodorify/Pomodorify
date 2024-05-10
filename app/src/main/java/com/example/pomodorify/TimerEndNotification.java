package com.example.pomodorify;

import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;


public class TimerEndNotification {
    private Context context;
    private NotificationManager notificationManager;

    public TimerEndNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    //TODO: 3 rodzaje metod, powiadomienie, powiadomieniebezdziwieku, powiadomieniedziekowe

    public void showNotification(){


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.timer)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



        //builder.setSilent(true);//jakiegos ifa mozna dodac i bez problemu sie wylacza dzwiek

        /*sam dzwiek
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.play();
         */


        notificationManager.notify(1, builder.build());
    }


}
