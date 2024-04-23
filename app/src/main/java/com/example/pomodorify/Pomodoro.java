package com.example.pomodorify;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Pomodoro extends Fragment{


    private Timer timer;//do odliczania czasu
    private Button btnStartStop;//zatrzymanie/start stopera
    private TextView timeLeft;//do wyswietlania czasu
    private RadioGroup radioGroup;

    public Pomodoro() {
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
        return inflater.inflate(R.layout.fragment_pomodoro, container, false);
    }

    private void switchCounting(){

    }

    private int getMinutesDatabase(int selectedId){
        return 0;
    }


    public void setFocus(){

    }

    public void setLongBreak(){

    }

    public void setShortBreak(){

    }
}