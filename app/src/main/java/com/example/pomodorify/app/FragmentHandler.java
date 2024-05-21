package com.example.pomodorify.app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pomodorify.R;

public class FragmentHandler {
    private final Fragment statistics;
    private final Fragment pomodoro;
    private final Fragment settings;
    private final FragmentManager fragmentManager;

    public FragmentHandler(Fragment statistics, Fragment pomodoro, Fragment settings, FragmentManager fragmentManager){
        this.statistics = statistics;
        this.pomodoro = pomodoro;
        this.settings = settings;
        this.fragmentManager = fragmentManager;
    }

    public Fragment getStatisticsFragment(){
        return statistics;
    }

    public Fragment getPomodoroFragment(){
        return pomodoro;
    }

    public Fragment getSettingsFragment(){
        return settings;
    }

    public Fragment getCurrentFragment(){
        return fragmentManager.findFragmentById(R.id.frame_layout);
    }

    public void replaceFragment(Fragment fragment){//np. dostaje statystyki i zmienia sobie
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //different animations based on origin and destination fragments
        if(getCurrentFragment() == getStatisticsFragment()){
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if(getCurrentFragment() == getSettingsFragment()){
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else{
            //Pomodoro Fragment is in the middle so there are two options
            if(fragment == getSettingsFragment()){
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            }else{
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}