package com.example.pomodorify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pomodorify.databinding.ActivityMainBinding;

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
        }

        //this.deleteDatabase("Pomodorify.db");
    }
}