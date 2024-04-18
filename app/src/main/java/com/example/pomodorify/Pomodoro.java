package com.example.pomodorify;

import androidx.fragment.app.Fragment;
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