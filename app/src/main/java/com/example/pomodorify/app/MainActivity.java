package com.example.pomodorify.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.pomodorify.database.DBHelper;
import com.example.pomodorify.database.GetDarkThemePreferences;
import com.example.pomodorify.databinding.ActivityMainBinding;
import com.example.pomodorify.pomodoro.Pomodoro;
import com.example.pomodorify.settings.Settings;
import com.example.pomodorify.statistics.Statistics;

public class MainActivity extends AppCompatActivity {

    public static boolean release;// If true then counting is in minutes, if false then counting is in seconds (for example for debbuging)

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        release = true;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavHandler bottomNavHandler = new BottomNavHandler(new FragmentHandler(new Statistics(), new Pomodoro(), new Settings(), getSupportFragmentManager()), binding);

        //for example after screen orientation changes, activity restarts and calls onCreate, in such situation we don't want to call those methods again
        if(savedInstanceState == null){
            bottomNavHandler.setDefaultSettings();
            createNotificationChannel();
        }

        applyDarkTheme();

        //this.deleteDatabase("Pomodorify.db"); -- to remove local database (just for debugging, will remove this line later)
    }

    @Override
    protected void onDestroy(){
        DBHelper.closeConnection();
        super.onDestroy();
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

    /*
        changes theme to dark theme UI if user selected dark theme in Settings
     */
    private void applyDarkTheme(){
        GetDarkThemePreferences getDarkThemePreferences = DBHelper.getInstance(this);
        if (getDarkThemePreferences.getDarkThemePreferences()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}