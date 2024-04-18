package com.example.pomodorify;

import android.view.MenuItem;
import com.example.pomodorify.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHandler implements BottomNavigationView.OnItemSelectedListener {

    FragmentHandler fragmentHandler;

    ActivityMainBinding binding;

    public BottomNavHandler() {

    }

    public BottomNavHandler(FragmentHandler fragmentHandler, ActivityMainBinding binding) {

    }

    public void setDefaultSettings(){
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
