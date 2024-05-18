package com.example.pomodorify;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


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

        //rozne animacje na przejscia z roznych fragmentow, rozpisujemy kombinacje
        if(getCurrentFragment() == getStatisticsFragment()){
            //pomodoro i settings sa po prawej
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if(getCurrentFragment() == getSettingsFragment()){
            //pomodoro i statistics sa po lewej
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else{
            //w zaleznosci od fragmentu animacja w prawo lub w lewo
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

//TODO: Mozna zrobic tak ze fragmenty ladujemy tylko raz i potem korzystamy z hide/show zamiast replace
//TODO: wtedy nie trzeba tez za kazdym razem ladowac statystyk z bazy danych, statystyki zaladujemy tylko
//TODO: na poczatku oraz po tym jak powiadomi nas o tym observer pattern; mozna tez uzyc notifyDataSetChanged() na ArrayAdapter
