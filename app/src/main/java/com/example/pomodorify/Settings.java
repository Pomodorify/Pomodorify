package com.example.pomodorify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Settings extends Fragment {

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}

//TODO: mamy 3 timery, dla kazdego wybierz: czy chcesz przerwe, dlugosc timera, dlugosc przerwy, ew jakis dark theme
//TODO: jest tez jakis fragmentsettings, moze lepiej z niego skorzystac zamiast zwykly fragment, byc moze lepiej ustawienia trzymac w sharedpreferences