package com.example.pomodorify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pomodorify.databinding.ActivityMainBinding;

//TODO: zapytanie o permisje, intent do ustawien powiadomien, sprawdz czy przydzielone powiadomienia
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavHandler bottomNavHandler = new BottomNavHandler(new FragmentHandler(new Statistics(), new Pomodoro(), new Settings(), getSupportFragmentManager()), binding);

        if(savedInstanceState == null){//jesli przywracam fragment (np. po restarcie aktywnosci po obrocie ekranu, to wtedy nie sie nie wywola)
            bottomNavHandler.setDefaultSettings();
            createNotificationChannel();
        }

        //this.deleteDatabase("Pomodorify.db"); 
    }

    /*
        Creates Notification Channel, execute this code as soon as your app starts.
        It's safe to call this repeatedly, because creating an existing notification channel performs no operation.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// Create the NotificationChannel, but only on API 26+;
            NotificationChannel channel = new NotificationChannel("1", "TimerEndNotification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel used to send send notifications after timer ends couting.");

            // Register the channel with the system.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}