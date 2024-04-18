package com.example.pomodorify;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class FragmentHandler {

    private final Fragment statistics;
    private final Fragment pomodoro;
    private final Fragment settings;
    private final FragmentManager fragmentManager;

    FragmentHandler(Fragment statistics, Fragment pomodoro, Fragment settings, FragmentManager fragmentManager){
        this.statistics = statistics;
        this.pomodoro = pomodoro;
        this.settings = settings;
        this.fragmentManager = fragmentManager;
    }

    public Fragment getStatisticsFragment(){
        return null;
    }
    
    public Fragment getPomodoroFragment(){
        return null;
    }

    public Fragment getSettingsFragment(){
        return null;
    }

    public void replaceFragment(Fragment fragment){

    }
}
