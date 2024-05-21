package com.example.pomodorify.app;

import android.view.MenuItem;

import com.example.pomodorify.R;
import com.example.pomodorify.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHandler implements BottomNavigationView.OnItemSelectedListener {

    FragmentHandler fragmentHandler;

    ActivityMainBinding binding;

    public BottomNavHandler(FragmentHandler fragmentHandler, ActivityMainBinding binding) {
        this.fragmentHandler = fragmentHandler;
        this.binding = binding;

        binding.bottomNavigationView.setOnItemSelectedListener(this);
    }

    public void setDefaultSettings(){
        binding.bottomNavigationView.getMenu().getItem(1).setChecked(true);

        fragmentHandler.replaceFragment(fragmentHandler.getPomodoroFragment());

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.statistics) {
            fragmentHandler.replaceFragment(fragmentHandler.getStatisticsFragment());
        } else if (item.getItemId() == R.id.pomodoro) {
            fragmentHandler.replaceFragment(fragmentHandler.getPomodoroFragment());
        } else if (item.getItemId() == R.id.settings) {
            fragmentHandler.replaceFragment(fragmentHandler.getSettingsFragment());
        }
        return true;
    }
}
