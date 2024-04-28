package com.example.pomodorify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        GetTimes getTimes = new DBHelper(getActivity());

        //ustawienie dlugosci timera focus
        SeekBar focusBar = view.findViewById(R.id.FocusBar);
        TextView focusDuration = view.findViewById(R.id.focusDuration);
        int focusTime = getTimes.getFocusTime();
        focusBar.setProgress(focusTime - 1);
        focusDuration.setText(String.valueOf(focusTime));
        focusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                focusDuration.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //wyslij do bazy danych
            }
        });

        //ustawienie dlugosci timera shortbreak
        SeekBar shortBar = view.findViewById(R.id.ShortBar);
        TextView shortDuration = view.findViewById(R.id.shortDuration);
        int shortTime = getTimes.getShortBreakTime();
        shortBar.setProgress(shortTime - 1);
        shortDuration.setText(String.valueOf(shortTime));
        shortBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shortDuration.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //wyslij do bazy danych
            }
        });

        //ustawienie dlugosci timera long break
        SeekBar longBar = view.findViewById(R.id.LongBar);
        TextView longDuration = view.findViewById(R.id.longDuration);
        int longTime = getTimes.getLongBreakTime();
        longBar.setProgress(longTime - 15);
        longDuration.setText(String.valueOf(longTime));
        longBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                longDuration.setText(String.valueOf(progress + 15));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //wyslij do bazy danych
            }
        });


        return view;
    }
}

//TODO: mamy 3 timery, dla kazdego wybierz: czy chcesz przerwe, dlugosc timera, dlugosc przerwy, ew jakis dark theme
//TODO: jest tez jakis fragmentsettings, moze lepiej z niego skorzystac zamiast zwykly fragment, byc moze lepiej ustawienia trzymac w sharedpreferences
//
//