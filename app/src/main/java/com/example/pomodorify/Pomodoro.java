package com.example.pomodorify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


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
        timer = null;
    }

    @Override
    public void onResume() {//to sie uruchomi po wyłączeniu i włączeniu ekranu jak i po zmienia zakladki
        super.onResume();

        if(timer == null){//jesli nie liczysz to wtedy ustaw czas w zaleznosci od tego co jest zaznaczone, jesli liczy to ontick to zmieni i tak
            int selectedId = getSelectedRadioId(radioGroup);
            int time = getMinutesDatabase(selectedId);

            timeLeft.setText(Utility.formatMillis(time * 1000));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null)
            timer.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        btnStartStop = view.findViewById(R.id.startStop);
        timeLeft = view.findViewById(R.id.timeLeft);
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCounting();
            }
        });


        radioGroup = view.findViewById(R.id.selectType);

        RadioButton FocusButton = radioGroup.findViewById(R.id.FocusButton);
        RadioButton sBreakButton = radioGroup.findViewById(R.id.sBreakButton);
        RadioButton lBreakButton = radioGroup.findViewById(R.id.lBreakButton);
        FocusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus();
            }
        });
        sBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShortBreak();
            }
        });
        lBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLongBreak();
            }
        });

        Button continueButton = view.findViewById(R.id.pauseButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        GetTimes getTimes = new DBHelper(getActivity());
        int time = getTimes.getFocusTime();
        timeLeft.setText(Utility.formatMillis((long)time * 1000));

        // Inflate the layout for this fragment
        return view;
    }

    private void switchCounting(){//jesli nie odlicza to zacznij odliczac, jesli odlicza to przerwij odliczac, na to jest ustawiony onclick

        //zamiast tego if .. else można dać zrobić state pattern: https://www.geeksforgeeks.org/state-design-pattern/
        if(timer == null){//zacznij liczyc

            //Odczytaj rodzaj aktywnosci focus/break
            int selectedId = getSelectedRadioId(radioGroup);

            //pobierz ilosc minut z bazy danych dla danej aktywnosci
            int minutes = getMinutesDatabase(selectedId);

            if(selectedId == getActivity().findViewById(R.id.FocusButton).getId()){//jesli sesja focus
                InsertStatistics insertStatistics = new DBHelper(getActivity());

                //pobierz etykiete aktywnosci
                EditText activity = getActivity().findViewById(R.id.activity);
                String activityLabel = activity.getText().toString();

                timer = new FocusTimer(minutes * 1000, 1000, timeLeft, insertStatistics, activityLabel);
            }
            else//jesli sesja break
                timer = new Timer(minutes * 1000, 1000, timeLeft);

            timer.setCustomObjectListener(new NotifyPomodoro(){
                @Override
                public void onFinish() {
                    resetCounting();
                }
            });
            //tutaj fajnie widać 3 zasade SOLID, nie wazne czy timer to Timer czy FocusTimer i tak sie poprawnie zachowa
            timer.start();

            setupCounting();
        }else{//przestan liczyc
            timer.cancel();
            resetCounting();
        }
    }

    private int getMinutesDatabase(int selectedId){
        GetTimes getTimes = new DBHelper(getActivity());
        if(selectedId == getActivity().findViewById(R.id.FocusButton).getId()){
            return getTimes.getFocusTime();
        }else if(selectedId == getActivity().findViewById(R.id.sBreakButton).getId()){
            return getTimes.getShortBreakTime();
        }else{
            return getTimes.getLongBreakTime();
        }
    }

    private void setupCounting(){//ustawia UI w tryb odliczania czyli np. pojawia sie przycisk pause

        btnStartStop.setText("Reset");

        enablePauseButton();

        disableButtons();
    }

    private void resetCounting(){//ustawia UI w tryb w ktorym nie odlicza
        timer = null;
        btnStartStop.setText("Start");

        disablePauseButton();

        enableButtons();

        Button continueButton = getActivity().findViewById(R.id.pauseButton);
        continueButton.setText("Pause");

        //zaaktualizuj timer, np. wpisz 25 jesli wybrany focus
        int selectedId = getSelectedRadioId(radioGroup);
        int time = getMinutesDatabase(selectedId);

        timeLeft.setText(Utility.formatMillis((long)time * 1000));
    }

    private void disableButtons(){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }

        EditText activity = getActivity().findViewById(R.id.activity);
        activity.setEnabled(false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setEnabled(false);
        }

    }

    private void enableButtons(){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
        }

        EditText activity = getActivity().findViewById(R.id.activity);
        activity.setEnabled(true);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setEnabled(true);
        }
    }

    public void setFocus(){
        GetTimes getTimes = new DBHelper(getActivity());
        int time = getTimes.getFocusTime();

        timeLeft.setText(Utility.formatMillis((long)time * 1000));
    }

    public void setLongBreak(){
        GetTimes getTimes = new DBHelper(getActivity());
        int time = getTimes.getLongBreakTime();

        timeLeft.setText(Utility.formatMillis((long)time * 1000));
    }

    public void setShortBreak(){
        GetTimes getTimes = new DBHelper(getActivity());
        int time = getTimes.getShortBreakTime();

        timeLeft.setText(Utility.formatMillis((long)time * 1000));
    }

    private void enablePauseButton(){
        Button continueButton = getActivity().findViewById(R.id.pauseButton);
        continueButton.setEnabled(true);
    }

    private void disablePauseButton(){
        Button continueButton = getActivity().findViewById(R.id.pauseButton);
        continueButton.setEnabled(false);
    }

    private int getSelectedRadioId(RadioGroup radioGroup){
        RadioButton radioButton = radioGroup.findViewById( radioGroup.getCheckedRadioButtonId());
        return radioButton.getId();
    }

    private void pauseTimer(){
        if(timer != null){
            if(!timer.isCancelled()){
                timer.cancel();
                Button continueButton = getActivity().findViewById(R.id.pauseButton);
                continueButton.setText("Resume");
            }else{
                timer.resume();
                Button continueButton = getActivity().findViewById(R.id.pauseButton);
                continueButton.setText("Pause");
            }
        }
    }
}