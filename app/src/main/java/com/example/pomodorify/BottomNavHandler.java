package com.example.pomodorify;

import android.view.MenuItem;
import com.example.pomodorify.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHandler implements BottomNavigationView.OnItemSelectedListener {

    FragmentHandler fragmentHandler;

    ActivityMainBinding binding;

    public BottomNavHandler(FragmentHandler fragmentHandler, ActivityMainBinding binding) {
        //wstrzykiwanie zaleznosci
        this.fragmentHandler = fragmentHandler;
        this.binding = binding;

        //dodaj eventlistner na bottom menu
        binding.bottomNavigationView.setOnItemSelectedListener(this);
    }

    public void setDefaultSettings(){
        //zaznacz srodkowy przycisk menu jako wybrany
        binding.bottomNavigationView.getMenu().getItem(1).setChecked(true);

        //wybierz fragment pomodoro jako domyslny
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
